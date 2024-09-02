package br.edu.ufabc.compiler.syntax;

import br.edu.ufabc.compiler.lexical.Analyzer;
import br.edu.ufabc.compiler.lexical.Token;

public class Parser {
	private Analyzer scanner;
	private Token    token;
	
	public Parser(Analyzer scanner) {
		this.scanner = scanner;
	}
	
	public void PROG() {
		token = scanner.nextToken();
		if (token.getType() != Token.RW || !token.getStr().equals("PROGRAM")) {
			throw new SyntaxException("Palavra reservada PROGRAM esperada, encontrei "+token.getStr()+ " linha "+scanner.getLine()+" coluna "+scanner.getColumn())	;		
		}
		token = scanner.nextToken();
		if (token.getType() != Token.ID) {
			throw new SyntaxException("Identificador de Programa esperado, encontrei "+token.getStr()+ " linha "+scanner.getLine()+" coluna "+scanner.getColumn());
		}
		token = scanner.nextToken();
		if (token.getType() != Token.PON) {
			throw new SyntaxException("; esperado, encontrei "+token.getStr() + " linha "+scanner.getLine()+" coluna "+scanner.getColumn());
		}
		E();
	}
	
	public void Declara() {
		token = scanner.nextToken();
		if (token.getType() != Token.RW || !token.getStr().equals("declare")) {
            		throw new SyntaxException("Palavra reservada 'declare' esperada, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
        	}

       		token = scanner.nextToken();
       		if (token.getType() != Token.ID) {
            		throw new SyntaxException("Identificador esperado após 'declare', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
        	}

		while (true) {
            		token = scanner.nextToken();
            		if (token != null && token.getType() == Token.PON && token.getStr().equals(",")) {
                		token = scanner.nextToken();
                		if (token.getType() != Token.ID) {
                    			throw new SyntaxException("Identificador esperado após ',' na declaração, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
                		}
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
            		if (token.getType() != Token.PON || !token.getStr().equals(".")) {
                		throw new SyntaxException("Ponto '.' esperado após comando, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
            		}
        	} while (token != null && (token.getType() == Token.ID || token.getType() == Token.RW));
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
	        if (!token.getStr().equals("(")) {
	            throw new SyntaxException("'(' esperado após 'leia', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	        }

	        token = scanner.nextToken();
	        if (token.getType() != Token.ID) {
	            throw new SyntaxException("Identificador esperado após '(', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	        }

	        token = scanner.nextToken();
	        if (token == !token.getStr().equals(")")) {
	            throw new SyntaxException("')' esperado após identificador, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	        }

        	token = scanner.nextToken();
    	}

	public void CmdEscrita() {
		token = scanner.nextToken();
	    	if (token.getType() != Token.PON || !token.getStr().equals("(")) {
			throw new SyntaxException("( esperado após 'escreva', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
		}	    
		token = scanner.nextToken();
		if (token == null) {
			throw new SyntaxException("Texto ou identificador esperado após '(', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
		}
		if (token.getType() == Token.ID || token.getType() == Token.RW) {
			token = scanner.nextToken();
			if (token.getType() != Token.PON || !token.getStr().equals(")")) {
		            throw new SyntaxException(") esperado após texto ou identificador, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
		        }
		} else {
			throw new SyntaxException("Texto ou identificador esperado após '(', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
		}
	}

	public void CmdIf() {
	    	//Verifica 'se'
	    	token = scanner.nextToken();
	    	if (token.getType() != Token.PON || !token.getStr().equals("(")) {
	        	throw new SyntaxException("( esperado após 'se', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	    	}
	
	    	//Analisa a expressão
	    	Expr();
	
	    	if (!isRelationalOperator(token.getStr())) {
	       		throw new SyntaxException("Operador relacional esperado após expressão, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	    	}
	    	token = scanner.nextToken();
	    	Expr();
	
	    	//Verifica 'entao'
	    	if (token.getType() != Token.RW || !token.getStr().equals("entao")) {
	        	throw new SyntaxException("Palavra reservada 'entao' esperada, encontrei " + token.getStr()  + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	    	}
	
	    	// Verifica o início do bloco de código
	    	token = scanner.nextToken();
	    	if (token.getType() != Token.PON || !token.getStr().equals("{")) {
	        	throw new SyntaxException("{ esperado após 'entao', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	    	}
	
	    	// Analisa o bloco de código do if
	    	Bloco();
	
	    	// Verifica 'senao'
	    	if (token.getType() == Token.RW && token.getStr().equals("senao")) {
	        	token = scanner.nextToken();
	        	if (token.getType() != Token.PON || !token.getStr().equals("{")) {
	            		throw new SyntaxException("{ esperado após 'senao', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	        	}
	
	        	// Analisa o bloco de código do else
	        	Bloco();
	    	}
	
	    	// Verifica o fim do bloco de código
	    	if ( token.getType() != Token.PON || !token.getStr().equals("}")) {
	        	throw new SyntaxException("} esperado após bloco 'se' ou 'senao', encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
	    	}
	}
	
	public void CmdExpr() {
    		if (token.getType() != Token.ID) {
        		throw new SyntaxException("Identificador esperado no comando de atribuição, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}
    
    		String id = token.getStr();
    		token = scanner.nextToken();
    		if (token.getType() != Token.ATT) {
        		throw new SyntaxException(":= esperado após identificador, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}
    
    		Expr(); // Processa a expressão à direita da atribuição
    
    		token = scanner.nextToken();
    		if (token.getType() != Token.PON) {
        		throw new SyntaxException("; esperado após atribuição, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}
	}

	public void Expr() {
    		Termo();
    		while (token != null && (token.getType() == Token.OP && (token.getStr().equals("+") || token.getStr().equals("-"))) ) {
        		token = scanner.nextToken();
        		Termo();
    		}
	}

	public void Termo() {
    		Fator();
    		while (token != null && (token.getType() == Token.OP && (token.getStr().equals("*") || token.getStr().equals("/"))) ) {
        		token = scanner.nextToken();
        		Fator();
    		}
	}

	public void Fator() {
    		if (token == null) {
        		throw new SyntaxException("Fator esperado, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
    		}

    		if (token.getType() == Token.NUM || token.getType() == Token.ID) {
        		token = scanner.nextToken();
    		} else if (token.getType() == Token.PON && token.getStr().equals("(")) {
        		token = scanner.nextToken();
        		Expr();
        		if (token.getType() != Token.PON || !token.getStr().equals(")")) {
            			throw new SyntaxException(") esperado após expressão, encontrei " + token.getStr() + " linha " + scanner.getLine() + " coluna " + scanner.getColumn());
        		}
        		token = scanner.nextToken();
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
	
}
