package calculator;

import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * A simple calculator.
 *
 * @author Willow Sapphire
 * @version 03/29/2020
 *
 */
public class Calculator
{
    public static final Pattern UNSIGNED_DOUBLE =
        Pattern.compile("((\\d+\\.?\\d*)|(\\.\\d+))([Ee][-+]?\\d+)?.*?");
    public static final Pattern CHARACTER = Pattern.compile("\\S.*?");

    /**
     * Calculates the answer to an infix expression.
     *
     * @param expression the expression to evaluate
     * @return the answer
     */
    public double calculate(String expression)
    {
        return evaluate(toPostfix(expression));
    }

    /**
     * Takes an infix expression and converts it to postfix.
     *
     * @param expression
     *            The infix expression.
     * @return the postfix expression.
     */
    protected String toPostfix(String expression)
    {
    	Stack<String> s = new Stack<String>();
        Scanner input = new Scanner("(" + expression + ")");
        String postfixExpression = "";
        while (input.hasNext())
        {
            // for a number
            if (input.hasNext(UNSIGNED_DOUBLE))
            {
                postfixExpression = addOn(postfixExpression,
                    input.findInLine(UNSIGNED_DOUBLE));
            }
            // for a non-number
            else
            {
                String next = input.findInLine(CHARACTER);
                char symbol = next.charAt(0);
                // for a left parentheses
                if (symbol == '(') {
                    s.push(next);
                }
                // for an operator
                else if (isOperator(symbol))
                {
                    while (!s.empty() && !s.peek().equals("(")
                        && !isLowerPrecedence(s.peek().charAt(0), symbol))
                    {
                        postfixExpression = addOn(postfixExpression, s.pop());
                    }
                    s.push(next);
                }
                // for a right parentheses
                else
                {
                    while (!s.empty() && !s.peek().equals("("))
                    {
                        postfixExpression = addOn(postfixExpression, s.pop());
                    }
                    if (s.empty() || !s.peek().equals("("))
                    {
                        postfixExpression = "error";
                        break;
                    }
                    s.pop();
                }
            }
        }
        input.close();
        return postfixExpression;
    }

    /**
     * Evaluates a postfix expression and returns the result.
     *
     * @param postfixExpression
     *            The postfix expression.
     * @return The result of the expression.
     */
    protected double evaluate(String postfixExpression)
    {
        Stack<Double> s = new Stack<Double>();
        Scanner input = new Scanner(postfixExpression);
        while (input.hasNext())
        {
            if (input.hasNext(UNSIGNED_DOUBLE))
            {
                s.push(Double.parseDouble(input.findInLine(UNSIGNED_DOUBLE)));
            }
            else
            {
                char operator = input.findInLine(CHARACTER).charAt(0);
                if (!isOperator(operator) || s.size() < 2)
                {
                    s.push(Double.NaN);
                    break;
                }
                double right = s.pop();
                s.push(combine(s.pop(), right, operator));
            }
        }
        input.close();
        return s.empty() ? Double.NaN : s.pop();

    }

    /**
     * Checks if a character is an operator of lower precedence than another.
     * @param a - the character to check
     * @param b - the character to compare against
     * @return - true if a is a lower precedence operation than b
     */
    private boolean isLowerPrecedence(char a, char b)
    {
        return (a == '/'|| a == '+') && (b == '-' || b == '*');
    }

    /**
     * Checks if a character is one of the four valid operators (+, -, /, *).
     * @param c - the character to check
     * @return - true if the character is a valid operator, false otherwise.
     */
    private boolean isOperator(char c)
    {
        return c == '+' || c == '/' || c == '-' || c == '*';
    }

    /**
     * Adds two strings together with a space between them.
     * @param a - the first string
     * @param b - the second string
     * @return - the two strings concatenated with a space between them
     */
    private String addOn(String a, String b)
    {
        return a + " " + b;
    }

    /**
     * Adds, subtracts, multiplies, or divides two operands.
     * @param left - the left operand
     * @param right - the right operand
     * @param op - the operator (+, -, /, *)
     * @return the result of the expression
     */
    private double combine(double left, double right, char op)
    {
        return op == '+' ? left + right
            : op == '-' ? left - right
            : op == '*' ? left * right
            : op == '/' ? left / right
            : Double.NaN;
    }
}
