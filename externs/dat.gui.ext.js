'use strict';

var DAT = {};


/**
 * @constructor
 * @param {DAT.GUIParams=} opt_option
 */
DAT.GUI = function(opt_option) {};


/**
 * @type {Array<DAT.GUIController>}
 */
DAT.GUI.prototype.___controllers;


/**
 * @type {Array<DAT.GUI>}
 */
DAT.GUI.prototype.___folders;


/**
 * @type {DAT.HTMLElement}
 */
DAT.GUI.prototype.domElement;


/**
 * @param {(DAT.Object|DAT.Object|DAT.Object|DAT.Object|DAT.Object|DAT.Object)} target
 * @param {(string|string|string|string|string|string)} propName
 * @param {(number|boolean|Array<string>|Array<number>|DAT.Object)} min
 * @param {number=} opt_max
 * @return {(DAT.GUIController|DAT.GUIController|DAT.GUIController|DAT.GUIController|DAT.GUIController|DAT.GUIController)}
 */
DAT.GUI.prototype.add = function(target, propName, min, opt_max) {};


/**
 * @param {(DAT.Object|DAT.Object|DAT.Object|DAT.Object)} target
 * @param {(string|string|string|string)} propName
 * @param {(string|Array<number>|[object Object])} color
 * @return {(DAT.GUIController|DAT.GUIController|DAT.GUIController|DAT.GUIController)}
 */
DAT.GUI.prototype.addColor = function(target, propName, color) {};


/**
 * @param {string} propName
 * @return {DAT.GUI}
 */
DAT.GUI.prototype.addFolder = function(propName) {};


/**

*/
DAT.GUI.prototype.close = function() {};


/**

*/
DAT.GUI.prototype.open = function() {};


/**
 * @param {DAT.Object} target
 */
DAT.GUI.prototype.remember = function(target) {};


/**
 * @interface
 */
DAT.GUIParams = function() {};


/**
 * @constructor
 */
DAT.GUIController = function() {};


/**

*/
DAT.GUIController.prototype.destroy = function() {};


/**
 * @return {DAT.GUIController}
 */
DAT.GUIController.prototype.fire = function() {};


/**
 * @return {*}
 */
DAT.GUIController.prototype.getValue = function() {};


/**
 * @return {boolean}
 */
DAT.GUIController.prototype.isModified = function() {};


/**
 * @return {DAT.GUIController}
 */
DAT.GUIController.prototype.listen = function() {};


/**
 * @param {number} n
 * @return {DAT.GUIController}
 */
DAT.GUIController.prototype.min = function(n) {};


/**
 * @param {DAT.GUIController} target
 */
DAT.GUIController.prototype.remove = function(target) {};


/**
 * @param {*} value
 * @return {DAT.GUIController}
 */
DAT.GUIController.prototype.setValue = function(value) {};


/**
 * @param {number} n
 * @return {DAT.GUIController}
 */
DAT.GUIController.prototype.step = function(n) {};


/**

*/
DAT.GUIController.prototype.updateDisplay = function() {};


/**
 * @type {function(opt_value:*)}
 */
DAT.GUIController.prototype.onChange;


/**
 * @type {function(opt_value:*)}
 */
DAT.GUIController.prototype.onFinishChange;
