package de.uniorg.ui5helper.binding;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.testFramework.LexerTestCase;

import java.io.Reader;


public class LexerTest extends LexerTestCase {

    @Override
    protected Lexer createLexer() {
        return new FlexAdapter(new BindingLexer((Reader) null));
    }

    @Override
    protected String getDirPath() {
        return null;
    }

    public void testExpressionBinding() {
        doTest(
                "{= ${ path: 'cart>to_art_classf', formatter: '.formatter.getGasketColor' } !== '0' &amp;&amp; ${ path: 'cart>to_art_classf', formatter: '.formatter.getGasketColor' } !== '' }",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_EXPRESSION_MARKER ('=')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.STRING ('path')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'cart>to_art_classf\'')\n" +
                        "BindingTokenType.T_COMMA (',')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.STRING ('formatter')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'.formatter.getGasketColor\'')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_NEEQEQ ('!==')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING (''0'')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_LOGIC_AND ('&amp;&amp;')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.STRING ('path')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING (''cart>to_art_classf'')\n" +
                        "BindingTokenType.T_COMMA (',')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.STRING ('formatter')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING (''.formatter.getGasketColor'')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_NEEQEQ ('!==')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('''')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"

        );
        doTest("{=!${visibility>/addAllToCart}}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_EXPRESSION_MARKER ('=')\n" +
                        "BindingTokenType.T_NOT_OPERATOR ('!')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('visibility')\n" +
                        "BindingTokenType.MODEL_SEP ('>')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('addAllToCart')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );
        doTest("{= ${user>/config/UI_CONTRACTKTEXT} ? ${Ktext} : ${Bstnk} }",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_EXPRESSION_MARKER ('=')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('user')\n" +
                        "BindingTokenType.MODEL_SEP ('>')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('config')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('UI_CONTRACTKTEXT')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUESTIONMARK ('?')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('Ktext')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('Bstnk')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );
        doTest(
                "{= ${my/Path}.isCool() === true }",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_EXPRESSION_MARKER ('=')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('my')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('Path')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "BindingTokenType.T_DOT ('.')\n" +
                        "BindingTokenType.STRING ('isCool')\n" +
                        "BindingTokenType.T_ROUND_OPEN ('(')\n" +
                        "BindingTokenType.T_ROUND_CLOSE (')')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EQEQEQ ('===')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_TRUE ('true')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"

        );
        doTest(
                "{= ${my/Path}.isCool() !== false }",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_EXPRESSION_MARKER ('=')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('my')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('Path')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "BindingTokenType.T_DOT ('.')\n" +
                        "BindingTokenType.STRING ('isCool')\n" +
                        "BindingTokenType.T_ROUND_OPEN ('(')\n" +
                        "BindingTokenType.T_ROUND_CLOSE (')')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_NEEQEQ ('!==')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_FALSE ('false')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"

        );
    }

    public void testComplexBinding() {
        doTest("{ path: 'cart>to_art_classf', formatter: '.formatter.getGasketColor' }",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.STRING ('path')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'cart>to_art_classf\'')\n" +
                        "BindingTokenType.T_COMMA (',')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.STRING ('formatter')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'.formatter.getGasketColor\'')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );
        doTest(
                "{ path: 'my/path', formatter: '.formatterFunction' }",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.STRING ('path')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'my/path\'')\n" +
                        "BindingTokenType.T_COMMA (',')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.STRING ('formatter')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'.formatterFunction\'')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );
        doTest(
                "{ parts:  [ '{my/path}', '{model>bla}' ], formatter: '.formatterFunction', paused: true }",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.STRING ('parts')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE ('  ')\n" +
                        "BindingTokenType.T_BRACKET_OPEN ('[')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'{my/path}\'')\n" +
                        "BindingTokenType.T_COMMA (',')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'{model>bla}\'')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_BRACKET_CLOSE (']')\n" +
                        "BindingTokenType.T_COMMA (',')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.STRING ('formatter')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'.formatterFunction\'')\n" +
                        "BindingTokenType.T_COMMA (',')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.STRING ('paused')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_TRUE ('true')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );
    }

    public void testComboBinding() {
        doTest(
                "{model>/path} asdf {model>/path}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('model')\n" +
                        "BindingTokenType.MODEL_SEP ('>')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('path')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "BindingTokenType.STRING (' asdf ')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('model')\n" +
                        "BindingTokenType.MODEL_SEP ('>')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('path')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"

        );
    }

    public void testSimpleBinding() {
        doTest(
                "{model>/path}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('model')\n" +
                        "BindingTokenType.MODEL_SEP ('>')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('path')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );

        doTest(
                "{model>/some/path here/123}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('model')\n" +
                        "BindingTokenType.MODEL_SEP ('>')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('some')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('path here')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('123')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );

        doTest(
                "{/some/path here/123}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('some')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('path here')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('123')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );

        doTest(
                "{some/path here/123}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('some')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('path here')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.STRING ('123')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );

        doTest(
                "{i18n>My.Crazy.Key}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('i18n')\n" +
                        "BindingTokenType.MODEL_SEP ('>')\n" +
                        "BindingTokenType.STRING ('My.Crazy.Key')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );
    }
}