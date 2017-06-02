/**
 * formart input time
 * @param {number} input - timestamp
 * @returns {string} formated time with suffix
 **/
define(['angular'], function() {

	'use strict';
	
	var formats = {
		'seconds': 1000,
		'minutes': 1000 * 60,
		'hours': 1000 * 60 * 60,
		'days': 1000 * 60 * 60 * 24,
		'month': 1000 * 60 * 60 * 24 * 30,
		'years': 1000 * 60 * 60 * 24 * 365,
		'centuries': 1000 * 60 * 60 * 24 * 365 * 100
	};

	var suffixes = {
		'seconds': '秒前',
		'minutes': '分钟前',
		'hours': '小时前',
		'days': '天前',
		'month': '个月前',
		'years': '年前',
		'centuries': '世纪前'
	};

	function created() {
		return function (input) {
			if (isNaN(input)) {
				return input;
			}

			var diff = Date.now() - input;

			if (diff < 0) {
				diff = 0;
			}
			
			var unitlist = ['seconds', 'minutes', 'hours', 'days', 'month', 'years', 'centuries'];
			var unit = unitlist.shift();
			var result = parseInt(diff / formats[unit]);

			while(unitlist.length && (diff / formats[unitlist[0]] > 1)) {
				unit = unitlist.shift();
				result = parseInt(diff / formats[unit]);
			}

			if(result == 0){
				return '刚刚';
			}else{
				return result.toString() +　suffixes[unit];
			}
		}
	}

	angular.module('app.filter.created',[])
		.filter('created', created);
});