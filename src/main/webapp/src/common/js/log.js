/**
 * @file Provide the basic logger util.
 *
 * @author: Lijie Chen (chenlijie@baidu.com)
 * @author: Leo Wang (wangkemiao@baidu.com)
 */

define(function(require) {
    'use strict';
    var $ = require('jquery');
    /**
     * The basic sticky params that inited at the very beginning and be added to
     * each log request. baseParams can be just initialized once for each
     * page.
     *
     * @type {Object}
     */
    var baseParams = {};

    /**
     * The extra sticky params that can be set to each log request, and can be
     * overrided again and again.
     *
     * @type {Object}
     */
    var extraParams = {};

    /**
     * The target of previous log request, which is used to organize the log
     * session in server side.
     * TODO(chenlijie): Is it good to use target to organize the session?
     *     There should be an EventId(ei) which is better for this purpose.
     *
     * @type {string}
     */
    var lastTarget = '';

    /**
     * The default path where this logger sends the request to.
     *
     * @type {string}
     */
    var logPath = '';

    /**
     * If true, return the content of form that send log data for unit test.
     * False by default. Can only be changed by logger.setDebugMode().
     *
     * @type {boolean}
     */
    var debugMode = false;

    /**
     * Copy (or call the funciton if it is) to key/value pair from the source
     * to the target.
     *
     * @param {Object} target The target object (the final result).
     * @param {Object} source The source object where data is from.
     */
    function copyParams(target, source) {
        for (var item in source) {
            target[item] = (typeof source[item] === 'function') ? source[item]() : source[item];
        }
    }

    /**
     * 跨域请求，在iframe中构建form表单提交
     *
     * @param {string} path     请求地址
     * @param {Object} params   请求参数
     * @return {string} the content of the form in debug mode, or empty string.
     */
    function internalSend(path, params) {
        var ifr = document.createElement('iframe');
        var idom = null;

        try {
            $(ifr).css({
                'position': 'absolute',
                'left': '-10000px',
                'top': '-10000px'
            });
            // We can use $('body').append(ifr). But this can't pass the
            // jasmine-node test since the jsdom mock is not perfect. It reports
            // error or 'Wrong Document'. So we use the native document API to
            // append the new document.
            document.getElementsByTagName('body')[0].appendChild(ifr);
            // Create the form content.
            var win = ifr.contentWindow || ifr; // 获取iframe的window对象
            idom = win.document; // 获取iframe的document对象
        } catch (e) {
            // 原始ie8下iframe性能的限制，有可能存在iframe未准备好，拒绝访问
            return;
        }

        var html = ['<form id="f" action="', path, '" method="POST">'];
        for (var item in params) {
            html.push('<input type="hidden" name="', item, '" value="',
                encodeURIComponent(params[item]), '"/>');
        }
        html.push('</form>');
        var formContent = html.join('');
        idom.open();
        idom.write(formContent);
        idom.close();

        // Submit the form.
        idom.getElementById('f').submit();
        ifr.onload = function() {
            setTimeout(function() {
                $(ifr).remove();
            }, 100);
        };
        return debugMode ? formContent : '';
    }

    /**
     * The exported logger.
     */
    var logger = {};

    /**
     * Initializes the logger with some basic params.
     *
     * @param {string} path The path where log is sent to.
     * @param {Object} params The basic params used for all the requests in the
     *     page.
     */
    logger.init = function(path, params) {
        // 基本参数在每次页面“刷新”时要先清空，以免带入脏数据。
        logPath = path;
        baseParams = {};
        if (params && typeof params === 'object') {
            copyParams(baseParams, params);
        } else {
            baseParams.userid = '';
            baseParams.optid = '';
            baseParams.path = '';
            baseParams.token = '';
        }
    };

    /**
     * Returns the current current page path.
     *
     * @return {string}
     */
    logger.getPagePath = function() {
        return baseParams.path || '';
    };

    /**
     * Sets the current current page path.
     *
     * @param {?string} path The path of the current page.
     */
    logger.setPagePath = function(path) {
        if (path) {
            baseParams.path = path;
        }
    };

    /**
     * Sets the extra params sticky params.
     *
     * @param {Object} params The extra params used for all the requests in the
     *     page, and could be overrided by the next calling of setExtraParams.
     */
    logger.setExtraParams = function(params) {
        extraParams = {};
        if (params && typeof params === 'object') {
            copyParams(extraParams, params);
        }
    };

    /**
     * Sends a log request to backend service.
     *
     * @param {Object} params The one-time params attached in this request.
     * @param {string} optTarget The target of this log request.
     * @param {boolean} optOnlyUseBasicParams If true, exclude extra params.
     *
     * @return {string} empty by default, or the form that send the real request
     *     in debug mode.
     */
    logger.log = function(params, optTarget, optOnlyUseBasicParams) {
        if (!logPath || !baseParams) {
            throw new Error('Logger has not been initialized.');
        }

        var logParams = {};
        copyParams(logParams, baseParams);
        if (!optOnlyUseBasicParams) {
            copyParams(logParams, extraParams);
        }
        copyParams(logParams, params);
        if (optTarget) {
            logParams.target = optTarget;
        }

        logParams.lastTarget = lastTarget;
        lastTarget = logParams.target || logParams.fn || lastTarget;
        return internalSend(logPath, logParams);
    };

    logger.setDebugMode = function(flag) {
        debugMode = flag;
    };

    logger.isDebugMode = function() {
        return debugMode;
    };

    return logger;
});