package syntax;

import lexical.Analyzer;
import lexical.Token;
import semantic.Simbol;
import semantic.SimbolsTable;

public class Parser {
	private Analyzer scanner;
	private Token token;
	private SimbolsTable simbolstable;
	private Simbol simbol

	private void checkTokenNull() {
    		if (token == null) {
        		throw new SyntaxException("Token inesperado EOF na linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}
	}
	
	public Parser(Analyzer scanner) {
		this.scanner = scanner;
	}
	
	public void PROG() {
		token = scanner.nextToken();
		checkTokenNull();
		
		if (token.getType() != Token.RW || !token.getStr().equals("PROGRAM")) {
			throw new SyntaxException("Palavra reservada PROGRAM esperada, encontrei "+token.getStr()+ " linha "+scanner.getLine()+" coluna "+scanner.getColumn())	;		
		}

		Declara();
		Bloco();
		
		token = scanner.nextToken();
		checkTokenNull();
		
		if (token.getType() != Token.ID) {
			throw new SyntaxException("Identificador de Programa esperado, encontrei "+token.getStr()+ " linha "+scanner.getLine()+" coluna "+scanner.getColumn());
		}
		
		token = scanner.nextToken();
		checkTokenNull();
		if (token.getType() != Token.PON) {
			throw new SyntaxException("; esperado, encontrei "+token.getStr() + " linha "+scanner.getLine()+" coluna "+scanner.getColumn());
		}
		E();
	}
	
	public void Declara() {
		token = scanner.nextToken();
		checkTokenNull();
		
		if (token.getType() != Token.RW || !token.getStr().equals("declare")) {
            		throw new SyntaxException("Palavra reservada 'declare' esperada, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
        	}

       		token = scanner.nextToken();
		checkTokenNull();
		
		//Capturando o tipo de info
		String tipo = token.getStr();

		if (!tipo.equals("int") && !tipo.equals("real") && !tipo.equals("string")) {
        		throw new SyntaxException("Tipo de dado inválido, encontrei " + tipo + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}

		token = scanner.nextToken();
		checkTokenNull();
		
       		if (token.getType() != Token.ID) {
            		throw new SyntaxException("Identificador esperado após 'tipo', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
        	}

		//Armazenar a variável e o tipo na tabela de símbolos
		Simbol simbol = new Simbol(token.getStr(), tipo);
    		simbolstable.addSimbol(token.getStr(), tipo);

		while (true) {
            		token = scanner.nextToken();
			checkTokenNull();
			
            		if (token.getType() == Token.PON && token.getStr().equals(",")) {
                		token = scanner.nextToken();
				checkTokenNull();
                		if (token.getType() != Token.ID) {
                    			throw new SyntaxException("Identificador esperado após ',' na declaração, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
                		}
				simbolstable.addSimbol(token.getStr(), tipo);
            		} else {
                		break;
            		}
        	}
		if (token.getType() != Token.PON || !token.getStr().equals(".")) {
            		throw new SyntaxException("Ponto final '.' esperado após declaração de variáveis, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
        	}
	}

	public void Bloco() {
        	do {
            		Cmd();
            		token = scanner.nextToken();
			checkTokenNull();
            		if (token.getType() != Token.PON || !token.getStr().equals(".")) {
                		throw new SyntaxException("Ponto '.' esperado após comando, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
            		}
        	} while (token.getType() == Token.ID || token.getType() == Token.RW);
    	}
	
    	public void Cmd() {
	        if (token.getType() == Token.RW) {
	            switch (token.getStr()) {
	                case "leia":
	                    CmdLeitura();
	                    break;
	                case "escreva":
	                    CmdEscrita();
	                    break;
	                case "se":
	                    CmdIf();
	                    break;
	                default:
	                    throw new SyntaxException("Comando inválido: " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	            }
	        } else if (token.getType() == Token.ID) {
	            CmdExpr();
	        } else {
	            throw new SyntaxException("Comando inválido, encontrado " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	        }
	}

    	public void CmdLeitura() {
	        token = scanner.nextToken();
		checkTokenNull();
		
	        if (!token.getStr().equals("(")) {
	            throw new SyntaxException("'(' esperado após 'leia', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	        }

	        token = scanner.nextToken();
		checkTokenNull();
		
	        if (token.getType() != Token.ID) {
	            throw new SyntaxException("Identificador esperado após '(', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	        }

	        token = scanner.nextToken();
		checkTokenNull();
		
	        if (token == !token.getStr().equals(")")) {
	            throw new SyntaxException("')' esperado após identificador, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	        }

        	token = scanner.nextToken();
		checkTokenNull();
    	}

	public void CmdEscrita() {
		token = scanner.nextToken();
		checkTokenNull();

		boolean novaLinha = token.getStr().equals("println");

		token = scanner.nextToken();
		checkTokenNull();
		
	    	if (token.getType() != Token.PON || !token.getStr().equals("(")) {
			throw new SyntaxException("( esperado após 'escreva', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
		}	
		
		token = scanner.nextToken();
		checkTokenNull();
		
		if (token == null) {
			throw new SyntaxException("Texto ou identificador esperado após '(', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
		}
		if (token.getType() == Token.ID || token.getType() == Token.RW) {
			token = scanner.nextToken();
			checkTokenNull();
			
			if (token.getType() != Token.PON || !token.getStr().equals(")")) {
		            throw new SyntaxException(") esperado após texto ou identificador, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
		        }
		} else {
			throw new SyntaxException("Texto ou identificador esperado após '(', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
		}

		if (novaLinha) {
        		System.out.println();
    		} else {
        		System.out.print();
    		}
	}

	public void CmdIfElse() {
	    	//Verifica 'se'
	    	token = scanner.nextToken();
		checkTokenNull();
		
	    	if (token.getType() != Token.PON || !token.getStr().equals("(")) {
	        	throw new SyntaxException("( esperado após 'se', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	    	}
	
	    	Expr(); //Primeira expressao
		Op_rel(); //Operador
		Expr(); //Segunda expressao

		token = scanner.nextToken();
		checkTokenNull();

		if (token.getType() != Token.PON || !token.getStr().equals(")")) {
	        	throw new SyntaxException("{ esperado após condição', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	    	}

		token = scanner.nextToken();
		checkTokenNull();
		// Verifica 'entao'
		if (token.getType() != Token.RW || !token.getStr().equals("entao")) {
	        	throw new SyntaxException("Palavra 'entao' esperada, encontrei " + token.getStr()  + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	    	}
	
	    	Bloco();
		token = scanner.nextToken();
		checkTokenNull();
		
	    	// Verifica 'senao'
	    	if (token.getType() == Token.RW && token.getStr().equals("senao")) {
	        	Bloco();
		}	
	}

	public void CmdWhile() {
        	token = scanner.nextToken();
        	checkTokenNull();
		
        	if (token.getType() != Token.PON || !token.getStr().equals("(")) {
            		throw new SyntaxException("'(' esperado após 'enquanto', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
        	}

        	Expr();  //Primeira expressão
        	Op_rel();  //Operador
        	Expr();  //Segunda expressão

        	token = scanner.nextToken();
        	checkTokenNull();
        	if (token.getType() != Token.PON || !token.getStr().equals(")")) {
            		throw new SyntaxException("')' esperado após condição, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
        	}

        	token = scanner.nextToken();
        	checkTokenNull();
        	if (token.getType() != Token.RW || !token.getStr().equals("{")) {
            		throw new SyntaxException("'{' esperado após condição em 'enquanto', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
        	}

        	Bloco();

        	token = scanner.nextToken();
        	checkTokenNull();
        	
		if (token.getType() != Token.RW || !token.getStr().equals("}")) {
            		throw new SyntaxException("'}' esperado ao final de 'enquanto', encontrei " + token.getStr() + " linha " + scanner
		}
	}

	public void CmdFor() {
    		token = scanner.nextToken();
    		checkTokenNull();
    		if (token.getType() != Token.PON || !token.getStr().equals("(")) {
        		throw new SyntaxException("'(' esperado após 'for', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}

    		CmdExpr();  // Atribuição inicial
    		if (!token.getStr().equals(";")) {
        		throw new SyntaxException("';' esperado após atribuição, encontrei " + token.getStr());
    		}

    		Expr();  // Condição de continuação
    		if (!token.getStr().equals(";")) {
        		throw new SyntaxException("';' esperado após expressão, encontrei " + token.getStr());
    		}

    		CmdExpr();  // Incremento ou decremento
    		if (!token.getStr().equals(")")) {
        		throw new SyntaxException("')' esperado, encontrei " + token.getStr());
    		}

    		Bloco();
	}

	public void CmdRepeat() {
    		token = scanner.nextToken();
    		checkTokenNull();

    		Bloco();

    		token = scanner.nextToken();
    		checkTokenNull();
		
    		if (token.getType() != Token.RW || !token.getStr().equals("até")) {
        		throw new SyntaxException("'até' esperado após bloco, encontrei " + token.getStr());
    		}
		
    		Expr();  // Condição de saída
	}
	
	public void CmdExpr() {
    		if (token.getType() != Token.ID) {
        		throw new SyntaxException("Identificador esperado no comando de atribuição, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}
    
    		String id = token.getStr();

		if (!simbolstable.checkSimbol(id)) {
        		throw new SyntaxException("Variável '" + id + "' usada, mas não foi declarada.");
    		}
		// Recupera o símbolo da variável
    		Simbol simbol = simbolstable.getSimbol(id);
		
    		//Marca a variável como usada
    		simbol.markAsUsed(id);
		
    		token = scanner.nextToken();
		checkTokenNull();
		
    		if (token.getType() != Token.ATT) {
        		throw new SyntaxException(":= esperado após identificador, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}
		//Processa a expressão e retorna seu tipo
		String tipoExpr = Expr(); 
    
		//Verifica compatibilidade de tipos
		String tipoVar = simbol.getType(id);
    		if (!tipoVar.equals(tipoExpr)) {
        		throw new SyntaxException("Tipo incompatível. Esperado " + tipoVariavel + ", encontrado " + tipoExpr);
    		}

		//Marca a variável como inicializada
    		simbol.markAsInicialized(id);
		
    		token = scanner.nextToken();
		checkTokenNull();
		
    		if (token.getType() != Token.PON) {
        		throw new SyntaxException("; esperado após atribuição, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}
	}

	public void Expr() {
		token = scanner.nextToken();
    		checkTokenNull();

    		if (token.getType() == Token.ID) {
        	String id = token.getStr();

        		if (!simbol.wasInitialized(id)) {
            			System.out.println("Warning: Variável '" + id + "' usada sem valor inicial.");
        		}

        		simbol.markAsUsed(id);
        		return simbol.getType(id);
		}
    		Termo();
    		while (token.getType() == Token.OP && (token.getStr().equals("+") || token.getStr().equals("-")) ) {
        		token = scanner.nextToken();
			checkTokenNull();
        		Termo();
    		}
	}

	public void Termo() {
    		Fator();
    		while (token.getType() == Token.OP && (token.getStr().equals("*") || token.getStr().equals("/")) ) {
        		token = scanner.nextToken();
			checkTokenNull();
        		Fator();
    		}
	}

	public void Fator() {
    		if (token == null) {
        		throw new SyntaxException("Fator esperado, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}

    		if (token.getType() == Token.NUM || token.getType() == Token.ID) {
        		token = scanner.nextToken();
			checkTokenNull();
			
    		} else if (token.getType() == Token.PON && token.getStr().equals("(")) {
        		token = scanner.nextToken();
			checkTokenNull();
			
        		Expr();
        		if (token.getType() != Token.PON || !token.getStr().equals(")")) {
            			throw new SyntaxException(") esperado após expressão, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
        		}
        		token = scanner.nextToken();
			checkTokenNull();
    		} else {
        		throw new SyntaxException("Número, identificador ou expressão entre parênteses esperado, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}
	}

	public void Texto() {
    		if (token.getType() != Token.TEXT) {
        		throw new SyntaxException("Texto esperado, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}
    		token = scanner.nextToken();
	}

	public void Num() {
    		if (token.getType() != Token.NUM) {
        		throw new SyntaxException("Número esperado, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}
    		token = scanner.nextToken();
	}

	public void Id() {
    		if (token.getType() != Token.ID) {
        		throw new SyntaxException("Identificador esperado, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}
    		token = scanner.nextToken();
	}

	public void VariaveisNaoUtilizadas() {
    		for (Simbol simbol : simbolstable.getSimbols()) {
        		if (!simbol.wasUsed()) {
            			System.out.println("Warning: Variável '" + id + "' foi declarada, mas não foi utilizada.");
        		}
    		}
	}
	
}
