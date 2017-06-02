/*!
 * Image (upload) dialog plugin for Editor.md
 *
 * @file        image-dialog.js
 * @author      pandao
 * @version     1.3.4
 * @updateTime  2015-06-09
 * {@link       https://github.com/pandao/editor.md}
 * @license     MIT
 *
 * version      2016-07-28 modify by Ellen Chia For OSS Storage
 */

(function () {

    var factory = function (exports) {

        var pluginName = "image-dialog";

        exports.fn.imageDialog = function () {

            var _this = this;
            var cm = this.cm;
            var lang = this.lang;
            var editor = this.editor;
            var settings = this.settings;
            var cursor = cm.getCursor();
            var selection = cm.getSelection();
            var imageLang = lang.dialog.image;
            var classPrefix = this.classPrefix;
            var formName = classPrefix + "image-form";
            var iframeName = classPrefix + "image-iframe";
            var fileInputName = classPrefix + "image-file";
            var dialogName = classPrefix + pluginName, dialog;

            cm.focus();

            var loading = function (show) {
                var _loading = dialog.find("." + classPrefix + "dialog-mask");
                _loading[(show) ? "show" : "hide"]();
            };

            if (editor.find("." + dialogName).length < 1) {
                var guid = (new Date).getTime();
                var action = settings.imageUploadURL + (settings.imageUploadURL.indexOf("?") >= 0 ? "&" : "?") + "guid=" + guid;

                if (settings.crossDomainUpload) {
                    action += "&callback=" + settings.uploadCallbackURL + "&dialog_id=editormd-image-dialog-" + guid;
                }

                var dialogContent = ( (settings.imageUpload) ? "<form name=\"" + formName + "\" id=\"" + formName + "\"  action=\"" + action + "\" target=\"" + iframeName + "\" method=\"post\" enctype=\"multipart/form-data\" class=\"" + classPrefix + "form\">" : "<div class=\"" + classPrefix + "form\">" ) +
                    ( (settings.imageUpload) ? "<iframe name=\"" + iframeName + "\" id=\"" + iframeName + "\" guid=\"" + guid + "\"></iframe>" : "" ) +
                    "<label>" + imageLang.url + "</label>" +
                    "<input type=\"text\" data-url readonly/>" + (function () {
                        return (settings.imageUpload) ? "<div class=\"" + classPrefix + "file-input\">" +
                        "<input type=\"file\" name=\"" + fileInputName + "\"  id=\"" + fileInputName + "\" accept=\"image/*\" />" +
                        "<input type=\"submit\" value=\"" + imageLang.uploadButton + "\" />" +
                        "</div>" : "";
                    })() +
                    "<br/>" +
                    // "<label>" + imageLang.alt + "</label>" +
                    // "<input type=\"text\" value=\"" + selection + "\" data-alt />" +
                    // "<br/>" +
                    // "<label>" + imageLang.link + "</label>" +
                    // "<input type=\"text\" value=\"http://\" data-link/>" +
                    // "<br/>" +
                    ( (settings.imageUpload) ? "</form>" : "</div>");

                dialog = this.createDialog({
                    title: imageLang.title,
                    width: (settings.imageUpload) ? 465 : 380,
                    height: 171,
                    name: dialogName,
                    content: dialogContent,
                    mask: settings.dialogShowMask,
                    drag: settings.dialogDraggable,
                    lockScreen: settings.dialogLockScreen,
                    maskStyle: {
                        opacity: settings.dialogMaskOpacity,
                        backgroundColor: settings.dialogMaskBgColor
                    },
                    buttons: {
                        enter: [lang.buttons.enter, function () {
                            var url = this.find("[data-url]").val();
                            var alt = this.find("[data-alt]").val() || "";
                            var link = this.find("[data-link]").val() || "";

                            if (url === "") {
                                alert(imageLang.imageURLEmpty);
                                return false;
                            }

                            var altAttr = (alt !== "") ? " \"" + alt + "\"" : "";

                            if (link === "" || link === "http://") {
                                cm.replaceSelection("![" + alt + "](" + url + altAttr + ")");
                            }
                            else {
                                cm.replaceSelection("[![" + alt + "](" + url + altAttr + ")](" + link + altAttr + ")");
                            }

                            // Not need it.
                            // if (alt === "") {
                            //     cm.setCursor(cursor.line, cursor.ch + 2);
                            // }

                            this.hide().lockScreen(false).hideMask();

                            return false;
                        }],

                        cancel: [lang.buttons.cancel, function () {
                            this.hide().lockScreen(false).hideMask();

                            return false;
                        }]
                    }
                });

                dialog.attr("id", classPrefix + "image-dialog-" + guid);

                if (!settings.imageUpload) {
                    return;
                }

                var fileInput = dialog.find("[name=\"" + classPrefix + "image-file\"]");

                fileInput.bind("change", function () {
                    var fileName = fileInput.val();
                    var _formats = [];
                    for (var index = 0; index < settings.imageFormats.length; index++) {
                        _formats.push(settings.imageFormats[index]);
                        _formats.push(settings.imageFormats[index].toUpperCase());
                    }
                    var isImage = new RegExp("(\\.(" + _formats.join("|") + "))$"); // /(\.(webp|jpg|jpeg|gif|bmp|png))$/

                    if (fileName === "") {
                        alert(imageLang.uploadFileEmpty);

                        return false;
                    }

                    if (!isImage.test(fileName)) {
                        alert(imageLang.formatNotAllowed + settings.imageFormats.join(", "));

                        return false;
                    }

                    loading(true);

                    var submitHandler = function () {
                        var files = document.getElementById(fileInputName).files;
                        settings.imageUploadPointcut(files[0], submitSuccessHandler);
                        return false; // Termination form submission
                    };

                    var submitSuccessHandler = function (link) {
                        dialog.find("[data-url]").val(link);
                        dialog.find("[data-link]").val(link);
                        loading(false);
                    };
                    // 20160803 fixed event repeated registration.
                    dialog.find("[type=\"submit\"]").unbind("click");
                    dialog.find("[type=\"submit\"]").bind("click", submitHandler).trigger("click");
                });
            }

            dialog = editor.find("." + dialogName);
            dialog.find("[type=\"text\"]").val("");
            dialog.find("[type=\"file\"]").val("");
            dialog.find("[data-link]").val("http://");

            this.dialogShowMask(dialog);
            this.dialogLockScreen();
            dialog.show();

        };

    };

    var define = undefined;
    // CommonJS/Node.js
    if (typeof require === "function" && typeof exports === "object" && typeof module === "object") {
        module.exports = factory;
    }
    else if (typeof define === "function")  // AMD/CMD/Sea.js
    {
        if (define.amd) { // for Require.js

            define(["editormd"], function (editormd) {
                factory(editormd);
            });

        } else { // for Sea.js
            define(function (require) {
                var editormd = require("./../../editormd");
                factory(editormd);
            });
        }
    }
    else {
        factory(window.editormd);
    }

})();