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
        doTest("{= ${user>/config/UI_CONTRACTKTEXT} ? ${Ktext} : ${Bstnk} }",
                "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_EXPRESSION_MARKER ('=')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_STRING ('user')\n" +
                        "BindingTokenType.T_MODEL_SEP ('>')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('config')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('UI_CONTRACTKTEXT')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUESTIONMARK ('?')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_STRING ('Ktext')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_STRING ('Bstnk')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')"
        );
        doTest(
                "{= ${my/Path}.isCool() === true }",
                "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_EXPRESSION_MARKER ('=')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_STRING ('my')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('Path')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')\n" +
                        "BindingTokenType.T_DOT ('.')\n" +
                        "BindingTokenType.T_STRING ('isCool')\n" +
                        "BindingTokenType.T_ROUND_OPEN ('(')\n" +
                        "BindingTokenType.T_ROUND_CLOSE (')')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EQEQEQ ('===')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_TRUE ('true')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')"

        );
        doTest(
                "{= ${my/Path}.isCool() !== false }",
                "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_EXPRESSION_MARKER ('=')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_STRING ('my')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('Path')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')\n" +
                        "BindingTokenType.T_DOT ('.')\n" +
                        "BindingTokenType.T_STRING ('isCool')\n" +
                        "BindingTokenType.T_ROUND_OPEN ('(')\n" +
                        "BindingTokenType.T_ROUND_CLOSE (')')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_NEEQEQ ('!==')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_FALSE ('false')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')"

        );
    }

    public void testComplexBinding() {
        doTest(
                "{ path: 'my/path', formatter: '.formatterFunction' }",
                "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_STRING ('path')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'my/path\'')\n" +
                        "BindingTokenType.T_COMMA (',')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_STRING ('formatter')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'.formatterFunction\'')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')"
        );
        doTest(
                "{ parts:  [ '{my/path}', '{model>bla}' ], formatter: '.formatterFunction', paused: true }",
                "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_STRING ('parts')\n" +
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
                        "BindingTokenType.T_STRING ('formatter')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_QUOTED_STRING ('\'.formatterFunction\'')\n" +
                        "BindingTokenType.T_COMMA (',')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_STRING ('paused')\n" +
                        "BindingTokenType.T_COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_TRUE ('true')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')"
        );
    }

    public void testComboBinding() {
        doTest(
                "{model>/path} asdf {model>/path}",
                "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_STRING ('model')\n" +
                        "BindingTokenType.T_MODEL_SEP ('>')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('path')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')\n" +
                        "BindingTokenType.T_STRING (' asdf ')\n" +
                        "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_STRING ('model')\n" +
                        "BindingTokenType.T_MODEL_SEP ('>')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('path')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')"

        );
    }

    public void testSimpleBinding() {
        doTest(
                "{model>/path}",
                "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_STRING ('model')\n" +
                        "BindingTokenType.T_MODEL_SEP ('>')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('path')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')"
        );

        doTest(
                "{model>/some/path here/123}",
                "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_STRING ('model')\n" +
                        "BindingTokenType.T_MODEL_SEP ('>')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('some')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('path here')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('123')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')"
        );

        doTest(
                "{/some/path here/123}",
                "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('some')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('path here')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('123')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')"
        );

        doTest(
                "{some/path here/123}",
                "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_STRING ('some')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('path here')\n" +
                        "BindingTokenType.T_PATH_SEP ('/')\n" +
                        "BindingTokenType.T_STRING ('123')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')"
        );

        doTest(
                "{i18n>My.Crazy.Key}",
                "BindingTokenType.T_CURLY_OPEN ('{')\n" +
                        "BindingTokenType.T_STRING ('i18n')\n" +
                        "BindingTokenType.T_MODEL_SEP ('>')\n" +
                        "BindingTokenType.T_STRING ('My.Crazy.Key')\n" +
                        "BindingTokenType.T_CURLY_CLOSE ('}')"
        );
    }
}