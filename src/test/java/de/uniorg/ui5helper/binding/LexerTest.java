package de.uniorg.ui5helper.binding;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.testFramework.LexerTestCase;

import java.io.Reader;


public class LexerTest extends LexerTestCase {

    private class ResultBuilder {
        private StringBuilder builder = new StringBuilder();

        ResultBuilder curlyOpen() {
            return this.add("BindingTokenType.CURLY_OPEN", "{");
        }

        ResultBuilder curlyClose() {
            return this.add("BindingTokenType.CURLY_CLOSE", "}");
        }

        ResultBuilder brackedOpen() {
            return this.add("BindingTokenType.BRACKET_OPEN", "[");
        }

        ResultBuilder brackedClose() {
            return this.add("BindingTokenType.BRACKET_CLOSE", "]");
        }

        ResultBuilder add(String type, String content) {
            return this.append(type).append(" ('").append(content).append("')\n");
        }

        String get() {
            return builder.toString();
        }

        ResultBuilder space() {
            return this.add("WHITE_SPACE", " ");
        }

        ResultBuilder whitespace(String content) {
            return this.add("WHITE_SPACE", content);
        }

        ResultBuilder string(String content) {
            return this.add("BindingTokenType.STRING", content);
        }

        ResultBuilder complexKey(String content) {
            return this.add("BindingTokenType.COMPLEX_BINDING_KEY", content);
        }

        ResultBuilder number(String content) {
            return this.add("BindingTokenType.NUMBER", content);
        }

        ResultBuilder colon() {
            return this.add("BindingTokenType.COLON", ":");
        }

        ResultBuilder quotedString(String content) {
            return this.add("BindingTokenType.QUOTED_STRING", "'" + content + "'");
        }

        ResultBuilder comma() {
            return this.add("BindingTokenType.COMMA", ",");
        }

        ResultBuilder pathSeparator() {
            return this.add("BindingTokenType.PATH_SEP", "/");
        }

        ResultBuilder pathSegment(String value) {
            return this.add("BindingTokenType.PATH_SEGMENT", value);
        }

        ResultBuilder modelSeparator() {
            return this.add("BindingTokenType.MODEL_SEP", ">");
        }

        ResultBuilder boolTrue() {
            return this.add("BindingTokenType.TRUE", "true");
        }

        ResultBuilder boolFalse() {
            return this.add("BindingTokenType.FALSE", "false");
        }

        ResultBuilder expression() {
            return this.add("BindingTokenType.EXPRESSION_MARKER", "=");
        }

        ResultBuilder embedded(String content) {
            return this
                    .add("BindingTokenType.EMBEDDED_MARKER", "$")
                    .curlyOpen()
                    .append(content)
                    .curlyClose();
        }

        ResultBuilder append(String content) {
            this.builder.append(content);

            return this;
        }

        ResultBuilder path(String... args) {
            for (int i = 0; i < args.length; i++) {
                this.pathSegment(args[i]);
                if (i < args.length - 1) {
                    this.pathSeparator();
                }
            }

            return this;
        }
    }

    @Override
    protected Lexer createLexer() {
        return new FlexAdapter(new BindingLexer((Reader) null));
    }

    @Override
    protected String getDirPath() {
        return null;
    }

    public ResultBuilder builder() {
        return new ResultBuilder();
    }

    public void testExpressionBinding() {
        doTest(
                "{= ${ path: 'cart>to_art_classf', formatter: '.formatter.getGasketColor' } !== '0' &amp;&amp; ${ path: 'cart>to_art_classf', formatter: '.formatter.getGasketColor' } !== '' }",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.EXPRESSION_MARKER ('=')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.COMPLEX_BINDING_KEY ('path')\n" +
                        "BindingTokenType.COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.QUOTED_STRING ('\'cart>to_art_classf\'')\n" +
                        "BindingTokenType.COMMA (',')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.COMPLEX_BINDING_KEY ('formatter')\n" +
                        "BindingTokenType.COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.QUOTED_STRING ('\'.formatter.getGasketColor\'')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.NEEQEQ ('!==')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.QUOTED_STRING (''0'')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.LOGIC_AND ('&amp;&amp;')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.EMBEDDED_MARKER ('$')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.COMPLEX_BINDING_KEY ('path')\n" +
                        "BindingTokenType.COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.QUOTED_STRING (''cart>to_art_classf'')\n" +
                        "BindingTokenType.COMMA (',')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.COMPLEX_BINDING_KEY ('formatter')\n" +
                        "BindingTokenType.COLON (':')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.QUOTED_STRING (''.formatter.getGasketColor'')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.NEEQEQ ('!==')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.QUOTED_STRING ('''')\n" +
                        "WHITE_SPACE (' ')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );

        doTest("{=!${visibility>/addAllToCart}}",
                builder()
                        .curlyOpen()
                        .expression()
                        .add("BindingTokenType.NOT_OPERATOR", "!")
                        .embedded(builder().string("visibility").modelSeparator().pathSeparator().pathSegment("addAllToCart").get())
                        .curlyClose().get()
        );

        doTest("{= ${user>/config/UI_CONTRACTKTEXT} ? ${Ktext} : ${Bstnk} }",
                builder()
                        .curlyOpen()
                        .expression()
                        .space()
                        .embedded(builder().string("user").modelSeparator().pathSeparator().path("config", "UI_CONTRACTKTEXT").get())
                        .space()
                        .add("BindingTokenType.QUESTIONMARK", "?")
                        .space()
                        .embedded(builder().string("Ktext").get())
                        .space()
                        .colon()
                        .space()
                        .embedded(builder().string("Bstnk").get())
                        .space()
                        .curlyClose()
                        .get()
        );
        doTest(
                "{= ${my/Path}.isCool() === true }",
                builder()
                        .curlyOpen()
                        .expression()
                        .space()
                        .embedded(builder().string("my").pathSeparator().pathSegment("Path").get())
                        .add("BindingTokenType.DOT", ".")
                        .string("isCool")
                        .add("BindingTokenType.ROUND_OPEN", "(")
                        .add("BindingTokenType.ROUND_CLOSE", ")")
                        .space()
                        .add("BindingTokenType.EQEQEQ", "===")
                        .space()
                        .boolTrue()
                        .space()
                        .curlyClose()
                        .get()

        );
        doTest(
                "{= ${my/Path}.isCool() !== false }",
                builder()
                        .curlyOpen()
                        .expression()
                        .space()
                        .embedded(builder().string("my").pathSeparator().pathSegment("Path").get())
                        .add("BindingTokenType.DOT", ".")
                        .string("isCool")
                        .add("BindingTokenType.ROUND_OPEN", "(")
                        .add("BindingTokenType.ROUND_CLOSE", ")")
                        .space()
                        .add("BindingTokenType.NEEQEQ", "!==")
                        .space()
                        .boolFalse()
                        .space()
                        .curlyClose()
                        .get()

        );
    }

    public void testComplexBinding() {
        doTest(
                "{ string: 'asdf', array: ['asdf', 2, true], object: { bool: true, quoted_string: 'geht auch' } }",
                builder()
                        .curlyOpen()
                        .space()
                        .complexKey("string").colon().space().quotedString("asdf").comma().space()
                        .complexKey("array").colon().space()
                            .brackedOpen()
                                .quotedString("asdf").comma().space()
                                .number("2").comma().space()
                                .boolTrue()
                            .brackedClose()
                            .comma()
                            .space()
                        .complexKey("object").colon().space()
                            .curlyOpen()
                            .space()
                        .complexKey("bool").colon().space().boolTrue().comma().space()
                        .complexKey("quoted_string").colon().space().quotedString("geht auch").space()
                            .curlyClose()
                        .space()
                        .curlyClose()
                        .get()
        );

        doTest(
                "{ path: 'cart>to_art_classf', formatter: '.formatter.getGasketColor' }",
                builder()
                        .curlyOpen().space()
                        .complexKey("path").colon().space().quotedString("cart>to_art_classf").comma().space()
                        .complexKey("formatter").colon().space().quotedString(".formatter.getGasketColor").space()
                        .curlyClose().get()
        );
        doTest(
                "{ path: 'my/path', formatter: '.formatterFunction' }",
                builder()
                        .curlyOpen().space()
                        .complexKey("path").colon().space().quotedString("my/path").comma().space()
                        .complexKey("formatter").colon().space().quotedString(".formatterFunction").space()
                        .curlyClose().get()
        );
        doTest(
                "{ parts: [ '{my/path}', '{model>bla}' ], formatter: '.formatterFunction', paused: true }",
                builder()
                        .curlyOpen().space()
                        .complexKey("parts").colon().space()
                        .brackedOpen().space()
                        .quotedString("{my/path}").comma().space()
                        .quotedString("{model>bla}").space()
                        .brackedClose().comma().space()
                        .complexKey("formatter").colon().space().quotedString(".formatterFunction").comma().space()
                        .complexKey("paused").colon().space().boolTrue().space()
                        .curlyClose().get()
        );
    }

    public void testComboBinding() {
        doTest(
                "{model>/path} asdf {model>/path}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('model')\n" +
                        "BindingTokenType.MODEL_SEP ('>')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.PATH_SEGMENT ('path')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')\n" +
                        "BindingTokenType.STRING (' asdf ')\n" +
                        "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('model')\n" +
                        "BindingTokenType.MODEL_SEP ('>')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.PATH_SEGMENT ('path')\n" +
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
                        "BindingTokenType.PATH_SEGMENT ('path')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );

        doTest(
                "{model>/some/path here/123}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('model')\n" +
                        "BindingTokenType.MODEL_SEP ('>')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.PATH_SEGMENT ('some')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.PATH_SEGMENT ('path here')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.PATH_SEGMENT ('123')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );

        doTest(
                "{/some/path here/123}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.PATH_SEGMENT ('some')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.PATH_SEGMENT ('path here')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.PATH_SEGMENT ('123')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );

        doTest(
                "{some/path here/123}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('some')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.PATH_SEGMENT ('path here')\n" +
                        "BindingTokenType.PATH_SEP ('/')\n" +
                        "BindingTokenType.PATH_SEGMENT ('123')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );

        doTest(
                "{i18n>My.Crazy.Key}",
                "BindingTokenType.CURLY_OPEN ('{')\n" +
                        "BindingTokenType.STRING ('i18n')\n" +
                        "BindingTokenType.MODEL_SEP ('>')\n" +
                        "BindingTokenType.PATH_SEGMENT ('My.Crazy.Key')\n" +
                        "BindingTokenType.CURLY_CLOSE ('}')"
        );
    }
}