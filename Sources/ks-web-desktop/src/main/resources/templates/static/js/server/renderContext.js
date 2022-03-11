'use strict';

//Vue = global.Vue;

var createRenderer = global.createRenderer;
process.env.NODE_ENV = 'production';
var renderer = createRenderer();

function render(template,data){

    var vm = {
        template: template,
        data: JSON.parse(data)
    }

    var str;
    renderer.renderToString(new Vue(vm), function (err,res) {
        if (err){
            console.error(err);
            throw  err;
        }
        str = res;
    });
    return str;
}

function renderVue$1(vue){

    var str;
    renderer.renderToString(vue, function (err,res) {
        if (err){
            console.error(err);
            throw  err;
        }
        str = res;
    });
    return str;
}

exports.renderVue = renderVue$1;

exports.Vue = Vue;

function $(cssQuery) {
    return document.selectFirst(cssQuery)
}

function openUrl(){ /* dummy */ }
function openPage(){ /* dummy */ }
function openError(){ /* dummy */ }
function confirm(){ /* dummy */ }
function prompt(){ /* dummy */ }
function createCookie(){ /* dummy */ }
function readCookie(){ /* dummy */ }
function eraseCookie(){ /* dummy */ }

var isBrowser = false;
var isServer = true;

renderVue$1;

//render;

