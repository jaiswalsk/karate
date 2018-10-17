/*
 * The MIT License
 *
 * Copyright 2017 Intuit Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.intuit.karate;

import cucumber.api.DataTable;
import cucumber.api.java.en.When;

import java.util.List;
import java.util.Map;

/**
 * the main purpose of this file is to keep ide-support happy (intellij /
 * eclipse) for feature-file formatting, auto-complete and syntax-coloring
 *
 * else all logic is in {@link ScenarioContext}
 *
 * the cucumber-eclipse plugin parses the TEXT of this file :( and we have to
 * have REAL text in the annotations instead of string-constants
 *
 * @author pthomas3
 */
public class StepActions implements Actions {

    public final ScenarioContext context;

    public StepActions(FeatureContext featureContext, CallContext callContext) {
        context = new ScenarioContext(featureContext, callContext);
    }
    
    public StepActions(ScenarioContext context) {
        this.context = context;
    }

    public static MatchType toMatchType(String eqSymbol, String each, String notContains, String only, boolean contains) {
        boolean notEquals = eqSymbol.startsWith("!");
        if (each == null) {
            if (notContains != null) {
                return MatchType.NOT_CONTAINS;
            }
            if (only != null) {
                return only.contains("only") ? MatchType.CONTAINS_ONLY : MatchType.CONTAINS_ANY;
            }
            return contains ? MatchType.CONTAINS : notEquals ? MatchType.NOT_EQUALS : MatchType.EQUALS;
        } else {
            if (notContains != null) {
                return MatchType.EACH_NOT_CONTAINS;
            }
            if (only != null) {
                return only.contains("only") ? MatchType.EACH_CONTAINS_ONLY : MatchType.EACH_CONTAINS_ANY;
            }
            return contains ? MatchType.EACH_CONTAINS : notEquals ? MatchType.EACH_NOT_EQUALS : MatchType.EACH_EQUALS;
        }
    }

    private static void validateEqualsSign(String eqSymbol) {
        if (eqSymbol.equals("=")) {
            throw new RuntimeException("use '==' for match (not '=')");
        }
    }

    @Override
    @When("^configure ([^\\s]+) =$")
    public void configureDocstring(String key, String exp) {
        context.configure(key, exp);
    }

    @Override
    @When("^configure ([^\\s]+) = (.+)")
    public void configure(String key, String exp) {
        context.configure(key, exp);
    }

    @Override
    @When("^url (.+)")
    public void url(String expression) {
        context.url(expression);
    }

    @Override
    @When("^path (.+)")
    public void path(List<String> paths) {
        context.path(paths);
    }

    @Override
    @When("^param ([^\\s]+) = (.+)")
    public void param(String name, List<String> values) {
        context.param(name, values);
    }

    @Override
    @When("^params (.+)")
    public void params(String expr) {
        context.params(expr);
    }

    @Override
    @When("^cookie ([^\\s]+) = (.+)")
    public void cookie(String name, String value) {
        context.cookie(name, value);
    }

    @Override
    @When("^cookies (.+)")
    public void cookies(String expr) {
        context.cookies(expr);
    }

    @Override
    @When("^header ([^\\s]+) = (.+)")
    public void header(String name, List<String> values) {
        context.header(name, values);
    }

    @Override
    @When("^headers (.+)")
    public void headers(String expr) {
        context.headers(expr);
    }

    @Override
    @When("^form field ([^\\s]+) = (.+)")
    public void formField(String name, List<String> values) {
        context.formField(name, values);
    }

    @Override
    @When("^form fields (.+)")
    public void formFields(String expr) {
        context.formFields(expr);
    }

    @Override
    @When("^request$")
    public void requestDocstring(String body) {
        context.request(body);
    }

    @Override
    @When("^request (.+)")
    public void request(String body) {
        context.request(body);
    }

    @When("^table (.+)")
    public void table(String name, DataTable table) {
        table(name, table.asMaps(String.class, String.class));
    }

    @Override
    @Action("^table (.+)")
    public void table(String name, List<Map<String, String>> table) {
        context.table(name, table);
    }

    @When("^replace (\\w+)$")
    public void replace(String name, DataTable table) {
        replace(name, table.asMaps(String.class, String.class));
    }

    @Override
    @Action("^replace (\\w+)$")
    public void replace(String name, List<Map<String, String>> table) {
        context.replace(name, table);
    }

    @Override
    @When("^replace (\\w+).([^\\s]+) = (.+)")
    public void replace(String name, String token, String value) {
        context.replace(name, token, value);
    }

    @Override
    @When("^def (.+) =$")
    public void defDocstring(String name, String expression) {
        context.assign(AssignType.AUTO, name, expression);
    }

    @Override
    @When("^def (\\w+) = (.+)")
    public void def(String name, String expression) {
        context.assign(AssignType.AUTO, name, expression);
    }

    @Override
    @When("^text (.+) =$")
    public void text(String name, String expression) {
        context.assign(AssignType.TEXT, name, expression);
    }

    @Override
    @When("^yaml (.+) =$")
    public void yaml(String name, String expression) {
        context.assign(AssignType.YAML, name, expression);
    }

    @Override
    @When("^copy (.+) = (.+)")
    public void copy(String name, String expression) {
        context.assign(AssignType.COPY, name, expression);
    }

    @Override
    @When("^json (.+) = (.+)")
    public void json(String name, String expression) {
        context.assign(AssignType.JSON, name, expression);
    }

    @Override
    @When("^string (.+) = (.+)")
    public void string(String name, String expression) {
        context.assign(AssignType.STRING, name, expression);
    }

    @Override
    @When("^xml (.+) = (.+)")
    public void xml(String name, String expression) {
        context.assign(AssignType.XML, name, expression);
    }

    @Override
    @When("^xmlstring (.+) = (.+)")
    public void xmlstring(String name, String expression) {
        context.assign(AssignType.XML_STRING, name, expression);
    }

    @Override
    @When("^assert (.+)")
    public void assertTrue(String expression) {
        context.assertTrue(expression);
    }

    @Override
    @When("^method (\\w+)")
    public void method(String method) {
        context.method(method);
    }

    @Override
    @When("^soap action( .+)?")
    public void soapAction(String action) {
        context.soapAction(action);
    }

    @Override
    @When("^multipart entity (.+)")
    public void multipartEntity(String value) {
        context.multipartField(null, value);
    }

    @Override
    @When("^multipart field (.+) = (.+)")
    public void multipartField(String name, String value) {
        context.multipartField(name, value);
    }

    @Override
    @When("^multipart fields (.+)")
    public void multipartFields(String expr) {
        context.multipartFields(expr);
    }

    @Override
    @When("^multipart file (.+) = (.+)")
    public void multipartFile(String name, String value) {
        context.multipartFile(name, value);
    }

    @Override
    @When("^multipart files (.+)")
    public void multipartFiles(String expr) {
        context.multipartFiles(expr);
    }

    @Override
    @When("^print (.+)")
    public void print(List<String> exps) {
        context.print(exps);
    }

    @Override
    @When("^status (\\d+)")
    public void status(int status) {
        context.status(status);
    }

    @Override
    @When("^match (each )?([^\\s]+)( [^\\s]+)? (==?|!=)$")
    public void matchEqualsDocstring(String each, String name, String path, String eqSymbol, String expected) {
        matchEquals(each, name, path, eqSymbol, expected);
    }

    @Override
    @When("^match (each )?([^\\s]+)( [^\\s]+)? (!)?contains( only| any)?$")
    public void matchContainsDocstring(String each, String name, String path, String not, String only, String expected) {
        matchContains(each, name, path, not, only, expected);
    }

    @Override
    @When("^match (each )?([^\\s]+)( [^\\s]+)? (==?|!=) (.+)")
    public void matchEquals(String each, String name, String path, String eqSymbol, String expected) {
        validateEqualsSign(eqSymbol);
        MatchType mt = toMatchType(eqSymbol, each, null, null, false);
        context.match(mt, name, path, expected);
    }

    @Override
    @When("^match (each )?([^\\s]+)( [^\\s]+)? (!)?contains( only| any)?(.+)")
    public void matchContains(String each, String name, String path, String not, String only, String expected) {
        MatchType mt = toMatchType("==", each, not, only, true);
        context.match(mt, name, path, expected);
    }

    @Override
    @When("^set ([^\\s]+)( .+)? =$")
    public void setDocstring(String name, String path, String value) {
        context.set(name, path, value);
    }

    @Override
    @When("^set ([^\\s]+)( .+)? = (.+)")
    public void set(String name, String path, String value) {
        context.set(name, path, value);
    }

    @When("^set ([^\\s]+)( [^=]+)?$")
    public void set(String name, String path, DataTable table) {
        set(name, path, table.asMaps(String.class, String.class));
    }

    @Override
    @Action("^set ([^\\s]+)( [^=]+)?$")
    public void set(String name, String path, List<Map<String, String>> table) {
        context.set(name, path, table);
    }

    @Override
    @When("^remove ([^\\s]+)( .+)?")
    public void remove(String name, String path) {
        context.remove(name, path);
    }

    @Override
    @When("^call ([^\\s]+)( .*)?")
    public void call(String name, String arg) {
        context.call(false, name, arg);
    }

    @Override
    @When("^callonce ([^\\s]+)( .*)?")
    public void callonce(String name, String arg) {
        context.call(true, name, arg);
    }

    @Override
    @When("^eval (.+)")
    public void eval(String exp) {
        context.eval(exp);
    }

    @Override
    @When("^eval$")
    public void evalDocstring(String exp) {
        context.eval(exp);
    }

    //==========================================================================

    @Override
    @When("^driver (.+)")
    public void driver(String expression) {
        context.driver(expression);
    }

    @Override
    @When("^location (.+)")
    public void location(String url) {
        context.location(url);
    }

    @Override
    @When("^input ([^\\s]+) = (.+)")
    public void input(String name, String value) {
        context.input(name, value);
    }
    
    @Override
    @When("^click (.+)")
    public void click(String name) {
        context.click(name);
    }  
    
    @Override
    @When("^submit (.+)")
    public void submit(String name) {
        context.submit(name);
    }  

}
