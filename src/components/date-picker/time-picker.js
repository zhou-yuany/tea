; (function ($) {
	//如果有元素移除
	$('.dr-contents').remove();
	$('body').append('<style>' +
		'.dr-contents {display:none;width: 100%;height:100%;z-index:1000;position:fixed;left:0;top:0;right:0;bottom:0;}' +
		'.dr-contents .bg{position:fixed;left:0;top:0;right:0;bottom:0;background:rgba(10, 10, 10, 0.2);z-index:1004;}' +
		'.dr-content {position:fixed;bottom:0;left:0;right:0;z-index:1005;}' +
		'.dr-content .btn{background:#ffffff;overflow:hidden;}' +
		'.dr-content .btn1{font-size:14px;float:left;padding:11px 0px;margin: 0 10px}' +
		'.dr-content .ok{float:right;color:#3BB8B3}' +
		'.dr-content .cancel{float:left;color:#333333}' +
		'.dr-content .name{text-align:center;font-size:14px;font-weight:bold;padding:11px 0;}' +
		'.dr-con {background:white;}' +
		'.dr-con .border{height:34px;border-top:1px solid #3BB8B3;border-bottom:1px solid #3BB8B3;border-width:1px 0;position:fixed;bottom:72px;left:0;right:0;pointer-events:none;}' +
		'.dr-con .table{display:table;width:100%;table-layout:fixed;}' +
		'.dr-con .cell{display:table-cell;vertical-align:middle;text-align:center;overflow:hidden;}' +
		'.dr-con .scroll{-webkit-overflow-scrolling:touch;height:180px;overflow:auto;box-sizing:border-box;padding:72px 0;width:200%;padding-right:100%;}' +
		'.dr-con .ele{font-size:16px;color:#b2b2b2;height:36px;line-height:37px;}' +
		'.dr-con .ele-active{font-size:16px;color:#333333;font-weight:bold;;height:36px;line-height:37px;}'+
		'@-webkit-keyframes mouseUp {from {opacity: 0;-webkit-transform: translate3d(0, 100%, 0);transform: translate3d(0, 100%, 0);}to {opacity: 1;-webkit-transform: none;transform: none;}}' +
		'@keyframes mouseUp {from {opacity: 0;-webkit-transform: translate3d(0, 100%, 0);transform: translate3d(0, 100%, 0);} to {opacity: 1;-webkit-transform: none;transform: none;}}' +
		'.mouseUp {-webkit-animation-name: mouseUp;animation-name: mouseUp;}' +
		'@-webkit-keyframes mouseDown {from {opacity: 1;-webkit-transform: none;transform: none;}to {opacity: 0;-webkit-transform: translate3d(0, 100%, 0);transform: translate3d(0, 100%, 0);}}' +
		'@keyframes mouseDown {from {opacity: 1;-webkit-transform: none;transform: none;}to {opacity: 0;-webkit-transform: translate3d(0, 100%, 0);transform: translate3d(0, 100%, 0);}}' +
		'.mouseDown {-webkit-animation-name: mouseDown;animation-name: mouseDown;}' +
		'.dr-animated {-webkit-animation-duration: .4s;animation-duration: .4s;-webkit-animation-fill-mode: both;animation-fill-mode: both;}' +
		'</style>' +
		'<div  class="dr-contents">' +
		' <div id="drContents">' +
		'   <div id="bg" class="bg"></div>' +
		'   <div id="mouseUp" class="dr-content dr-animated mouseUp">' +
		'       <div class="btn">' +
		'           <div class="btn1 ok">确定</div>' +
		'           <div class="btn1 cancel">取消</div>' +
		'           <div class="name">加载中...</div>' +
		'       </div>' +
		'       <div class="dr-con">' +
		'           <div class="border"></div>' +
		'           <div class="table"></div>' +
		'       </div>' +
		'   </div>' +
		'  </div>' +
		'</div>');

	// 取消选择
	$('.dr-content .cancel,.dr-contents .bg').click(function () {
		$('.dr-contents .bg')[0].removeEventListener('touchmove', preventDefaultThing, false);
		$('.dr-contents .btn')[0].removeEventListener('touchmove', preventDefaultThing, false);
		$('.dr-contents').find('.dr-content').removeClass('mouseUp').addClass('mouseDown');
		setTimeout(function () {
			$('.dr-contents').hide();
		}, 300);
	});

	//取消ios在zepto下的穿透事件
	$(".dr-con").on("touchend", function (event) {
		if (event.cancelable) {
			event.preventDefault();
		}
	});

	//取消默认行为.灰层底部不能滑动
	var preventDefaultThing = function (e) {
		if (e.cancelable) {
			e.preventDefault();
		}
		return false;
	};
	//新增或删除的dom
	function htmlOk(text){
		var div1 = document.createElement('div');
				div1.innerHTML = text;
				div1.setAttribute('class','ele')
		return div1
	}
	//新增删除dom操作
	function dayNumbers(scText,scText2){
		let num = null,dom = null;
		if (scText) {
			num = $('.dr-con .table').find('.elem')[2].children[0].children.length
			dom= $('.dr-con .table').find('.elem')[2].children[0]
		} else {
			num = $('.dr-con .table').find('.elem')[1].children[0].children.length
			dom= $('.dr-con .table').find('.elem')[1].children[0]
		}
		if (getDays(scText, scText2) == 31) {
			if (num == 30) {		
				dom.appendChild(htmlOk('31'))		
			}	else if(num == 29){
				dom.appendChild(htmlOk('30'))					
				dom.appendChild(htmlOk('31'))		
			}	else if(num == 28){
				dom.appendChild(htmlOk('29'))					
				dom.appendChild(htmlOk('30'))					
				dom.appendChild(htmlOk('31'))					
			}	
		} else if (getDays(scText, scText2) == 30) {
			if (num == 31) {
				dom.removeChild(dom.childNodes[30]);			
			}	else if(num == 29){
				dom.appendChild(htmlOk('30'))						
			}	else if(num == 28){
				dom.appendChild(htmlOk('29'))					
				dom.appendChild(htmlOk('30'))								
			}	
		} else if (getDays(scText, scText2) == 29) {
			if (num == 31) {
				dom.removeChild(dom.childNodes[30]);
				dom.removeChild(dom.childNodes[29]);			
			}	else if(num == 30){
				dom.removeChild(dom.childNodes[29]);					
			}	else if(num == 28){
				dom.appendChild(htmlOk('29'))											
			}	
		} else if (getDays(scText, scText2) == 28) {
			if (num == 31) {
				dom.removeChild(dom.childNodes[30]);
				dom.removeChild(dom.childNodes[29]);
				dom.removeChild(dom.childNodes[28]);			
			}	else if(num == 30){			
				dom.removeChild(dom.childNodes[29]);
				dom.removeChild(dom.childNodes[28]);		
			}	else if(num == 29){		
				dom.removeChild(dom.childNodes[28]);						
			}	
		}
	}
	//动态显示的html
	function htmlModel(ele) {
		// ele数组转换成相应结构
		var eleText = '';
		for (var i = 0; i < ele.length; i++) {
			eleText += '<div class="ele">' + ele[i] + '</div>';
		};
		return '<div class="cell elem"><div class="scroll">' + eleText + '</div></div>';
	};
	//每个月的天数
	function getDays(year, month) {
		if (year) {
			return new Date(year, month, 0).getDate();
		} else {
			return new Date(new Date().getFullYear(), month, 0).getDate();
		}
	}
	//天数小于10天在前面加"0"
	function add0(n) {
		let result = null
		if (n < 10) {
			result = '0' + n 
		} else {
			result = n + ''
		}
		return result
	}
	//天数转换成数组
	function dayAll(n) {
		var dayArrays = [];
		for (var i = 1; i <= n; i++) {
			dayArrays.push(add0(i));
		};
		return dayArrays;
	}

//-------------滚动监听，添加默认选中样式--------------
	function modelOne(key,value) {
		$('.dr-con').find('.elem').eq(key).find('.ele').each(function (indexs,emet) {
			if ($(this).text() == value) {
				$(this).parents('.scroll')[0].scrollTop = $(this).index() * 36;
				$(emet).addClass('ele-active')
			}
		});
	}

	// 选择器 适合场景，适用于年 月 日 小时 分钟 秒 选择
	$.timePicker = function (params) {
		var yearArray = [];
		var dayArr = params.dayArr || []; //当 dateType 为7 的时候，才有用
		var ele = params.ele || '.dr-date';

		var year = params.year || new Date().getFullYear();
		var month = params.month || new Date().getMonth() + 1;
		var day = params.day || new Date().getDate();
		var hour = params.hour || new Date().getHours();
		var minute = params.minute || new Date().getMinutes();
		var second = params.second || new Date().getSeconds();
		
		var startYear = params.startYear || ''; //开始年份
		var endYear = params.endYear || ''; //结束年份
		var dateType = params.dateType || 0; //时间类型
		
		var title = params.title || '日期和时间'; //弹起框标题
		var beforeThing = params.beforeThing || function () { }; //执行前的操作             
		var afterThing = params.afterThing || function () { };//执行后的操作

		// 年 默认范围：当前年份-10 ~ 当前年份 ~ 当前年份+10
		if (startYear !== '' && endYear !== '') {
			for (var i = startYear; i <= endYear; i++) {
				yearArray.push(i)
			};
		} else {
			for (var i = -10; i < 10; i++) {
				yearArray.push(new Date().getFullYear() - i)
			};
		}

		// 月 范围：十二个月份
		var tweMonth = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'];
		// 日 获取日期
		var dayArray = [];
		// 小时
		var timeHour = ["00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"];
		// 分钟
		var timeMinute = ["00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"];
		// 秒
		var timeSecond = ["00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"];
		
		// 添加初始值
		$(ele).attr('data-sel01', year);
		$(ele).attr('data-sel02', month);
		$(ele).attr('data-sel03', day);
		$(ele).attr('data-sel04', hour);
		$(ele).attr('data-sel05', minute);
		$(ele).attr('data-sel06', second);

		// 年月日选择器
		$(ele).on('click', function () {
			$('.dr-contents .bg')[0].addEventListener('touchmove', preventDefaultThing, false);
			$('.dr-contents .btn')[0].addEventListener('touchmove', preventDefaultThing, false);
			beforeThing();
			var htmlStr = '';//是否显示时分秒 0年月日 时分秒 1年月日 时分 2年月日时 3年月日 4年月 5年 6月日 7时分秒 8时分
			if (dateType == 0) {
				htmlStr = htmlModel(yearArray) + htmlModel(tweMonth) + htmlModel(dayAll(getDays(year, month))) + htmlModel(timeHour) + htmlModel(timeMinute) + htmlModel(timeSecond);
			} else if (dateType == 1) {
				htmlStr = htmlModel(yearArray) + htmlModel(tweMonth) + htmlModel(dayAll(getDays(year, month))) + htmlModel(timeHour) + htmlModel(timeMinute)
			} else if (dateType == 2) {
				htmlStr = htmlModel(yearArray) + htmlModel(tweMonth) + htmlModel(dayAll(getDays(year, month))) + htmlModel(timeHour)
			} else if (dateType == 3) {
				htmlStr = htmlModel(yearArray) + htmlModel(tweMonth) + htmlModel(dayAll(getDays(year, month)))
			} else if (dateType == 4) {
				htmlStr = htmlModel(yearArray) + htmlModel(tweMonth)
			} else if (dateType == 5) {
				htmlStr = htmlModel(yearArray)
			} else if (dateType == 6) {
				htmlStr = htmlModel(tweMonth)
			} else if (dateType == 7) {
				htmlStr = dayArr.length?htmlModel(dayArr):htmlModel(dayAll(getDays(year, month)))
			} else if (dateType == 8) {
				htmlStr = htmlModel(timeHour)
			} else if (dateType == 9) {
				htmlStr = htmlModel(timeMinute)
			} else if (dateType == 10) {
				htmlStr = htmlModel(timeSecond)
			} else if (dateType == 11) {
				htmlStr = htmlModel(timeHour) + htmlModel(timeMinute) + htmlModel(timeSecond);
			} else if (dateType == 12) {
				htmlStr = htmlModel(timeHour) + htmlModel(timeMinute);
			} else if (dateType == 13) {
				htmlStr = htmlModel(tweMonth) + htmlModel(dayAll(getDays(year, month))) + htmlModel(timeHour) + htmlModel(timeMinute);
			}

			$('.dr-con .table').html(htmlStr);
			$('.dr-content .name').text(title);
			$('.dr-contents').show().find('.dr-content').removeClass('mouseDown').addClass('mouseUp');

			// 选择器
			if ($(ele).val() != '') {
				year = $(ele).attr('data-sel01');
				month = $(ele).attr('data-sel02');
				day = $(ele).attr('data-sel03');
				hour = $(ele).attr('data-sel04');
				minute = $(ele).attr('data-sel05');
				second = $(ele).attr('data-sel06');
			}

			var scText = year; // 年
			var scText2 = month; // 月
			var scText3 = day; // 日
			var scText4 = hour; // 小时
			var scText5 = minute; // 分钟
			var scText6 = second; // 秒

				if (dateType == 0) {
					modelOne(0,year)
					modelOne(1,month)
					modelOne(2,day)
					modelOne(3,hour)
					modelOne(4,minute)
					modelOne(5,second)
				};
				if (dateType == 1) {
					modelOne(0,year)
					modelOne(1,month)
					modelOne(2,day)
					modelOne(3,hour)
					modelOne(4,minute)
				};
				if (dateType == 2) {
					modelOne(0,year)
					modelOne(1,month)
					modelOne(2,day)
					modelOne(3,hour)
				};
				if (dateType == 3 ) {
					modelOne(0,year)
					modelOne(1,month)
					modelOne(2,day)
				};
				if (dateType == 4) {
					modelOne(0,year)
					modelOne(1,month)
				};
				if (dateType == 5) {
					modelOne(0,year)
				};
				if (dateType == 6) {
					modelOne(0,month)
				}
				if (dateType == 7) {
					modelOne(0,day)
				}
				if (dateType == 8) {
					modelOne(0,hour)
				}
				if (dateType == 9) {
					modelOne(0,minute)
				}
				if (dateType == 10) {
					modelOne(0,second)
				}
				if (dateType == 11) {
					modelOne(0,hour)
					modelOne(1,minute)
					modelOne(2,second)
				}
				if (dateType == 12) {
					modelOne(0,hour)
					modelOne(1,minute)
				}
				if (dateType == 13) {
					modelOne(0,month)
					modelOne(1,day)
					modelOne(2,hour)
					modelOne(3,minute)
				}
				
				if (dateType == 0) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText = $(this).find('.ele').eq(scNum).text();
						
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						dayNumbers(scText,scText2)
						
						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});	

						$('.dr-con').find('.elem').eq(2).find('.ele').each(function () {
							if (Number($(this).text()) <= Number(scText3)) {
								$(this).parents('.scroll')[0].scrollTop = $(this).index() * 36;
							}
						});
					});
					$('.dr-con .scroll').eq(1).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText2 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						dayNumbers(scText,scText2)

						$('.dr-con').find('.elem').eq(1).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText2) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});

						$('.dr-con').find('.elem').eq(2).find('.ele').each(function () {
							if (Number($(this).text()) <= Number(scText3)) {
								$(this).parents('.scroll')[0].scrollTop = $(this).index() * 36;
							}
						});

					});
					$('.dr-con .scroll').eq(2).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText3 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(2).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText3) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(3).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText4 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(3).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText4) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(4).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText5 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(4).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText5) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(5).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText6 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						$('.dr-con').find('.elem').eq(5).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText6) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 1) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText = $(this).find('.ele').eq(scNum).text();
						
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						dayNumbers(scText,scText2)
						
						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});	

						$('.dr-con').find('.elem').eq(2).find('.ele').each(function () {
							if (Number($(this).text()) <= Number(scText3)) {
								$(this).parents('.scroll')[0].scrollTop = $(this).index() * 36;
							}
						});

					});
					$('.dr-con .scroll').eq(1).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText2 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						dayNumbers(scText,scText2)

						$('.dr-con').find('.elem').eq(1).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText2) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});

						$('.dr-con').find('.elem').eq(2).find('.ele').each(function () {
							if (Number($(this).text()) <= Number(scText3)) {
								$(this).parents('.scroll')[0].scrollTop = $(this).index() * 36;
							}
						});

					});
					$('.dr-con .scroll').eq(2).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText3 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(2).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText3) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(3).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText4 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(3).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText4) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(4).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText5 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(4).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText5) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 2) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText = $(this).find('.ele').eq(scNum).text();
						
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						dayNumbers(scText,scText2)
						
						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});	

						$('.dr-con').find('.elem').eq(2).find('.ele').each(function () {
							if (Number($(this).text()) <= Number(scText3)) {
								$(this).parents('.scroll')[0].scrollTop = $(this).index() * 36;
							}
						});

					});
					$('.dr-con .scroll').eq(1).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText2 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						dayNumbers(scText,scText2)

						$('.dr-con').find('.elem').eq(1).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText2) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});

						$('.dr-con').find('.elem').eq(2).find('.ele').each(function () {
							if (Number($(this).text()) <= Number(scText3)) {
								$(this).parents('.scroll')[0].scrollTop = $(this).index() * 36;
							}
						});

					});
					$('.dr-con .scroll').eq(2).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText3 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(2).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText3) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(3).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText4 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(3).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText4) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 3) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText = $(this).find('.ele').eq(scNum).text();
						
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						dayNumbers(scText,scText2)
						
						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});	

						$('.dr-con').find('.elem').eq(2).find('.ele').each(function () {
							if (Number($(this).text()) <= Number(scText3)) {
								$(this).parents('.scroll')[0].scrollTop = $(this).index() * 36;
							}
						});

					});
					$('.dr-con .scroll').eq(1).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText2 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						dayNumbers(scText,scText2)

						$('.dr-con').find('.elem').eq(1).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText2) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});

						$('.dr-con').find('.elem').eq(2).find('.ele').each(function () {
							if (Number($(this).text()) <= Number(scText3)) {
								$(this).parents('.scroll')[0].scrollTop = $(this).index() * 36;
							}
						});

					});
					$('.dr-con .scroll').eq(2).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText3 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(2).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText3) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 4) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText = $(this).find('.ele').eq(scNum).text();
						
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(1).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText2 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						$('.dr-con').find('.elem').eq(1).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText2) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 5) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText = $(this).find('.ele').eq(scNum).text();
						
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 6) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText2 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText2) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 7) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText3 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText3) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 8) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText4 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText4) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 9) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText5 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText5) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 10) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText6 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText6) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 11) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText4 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText4) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(1).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText5 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(1).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText5) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(2).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText6 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(2).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText6) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 12) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText4 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText4) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(1).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText5 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(1).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText5) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
				if (dateType == 13) {
					$('.dr-con .scroll').eq(0).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText2 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));

						dayNumbers(null,scText2)

						$('.dr-con').find('.elem').eq(0).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText2) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(1).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText3 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(1).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText3) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(2).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText4 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(2).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText4) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
					$('.dr-con .scroll').eq(3).scroll(function () {
						var that = $(this);
						// 数值显示
						var scTop = $(this)[0].scrollTop + 18;
						var scNum = Math.floor(scTop / 36);
						// 类型名称
						scText5 = $(this).find('.ele').eq(scNum).text();
						// 停止锁定
						clearTimeout($(this).attr('timer'));
						$(this).attr('timer', setTimeout(function () {
							that[0].scrollTop = scNum * 36;
						}, 100));
						$('.dr-con').find('.elem').eq(3).find('.ele').each(function (indexs,emet) {
							if ($(this).text() == scText5) {
								$(emet).addClass('ele-active')
							} else {
								$(emet).removeClass('ele-active')
							}
						});
					});
				}
			//移除之前的绑定事件
			$(".dr-content .ok").off();
			// 进行传值
			$('.dr-content .ok').click(function () {
				$(ele).attr('data-sel01', scText);
				$(ele).attr('data-sel02', scText2);
				$(ele).attr('data-sel03', scText3);
				$(ele).attr('data-sel04', scText4);
				$(ele).attr('data-sel05', scText5);
				$(ele).attr('data-sel06', scText6);

				afterThing(scText, scText2, scText3, scText4, scText5, scText6);

				$('.dr-contents').find('.dr-content').removeClass('mouseUp').addClass('mouseDown');
				setTimeout(function () {
					$('.dr-contents').hide();
				}, 300);

				$('.dr-contents .bg')[0].removeEventListener('touchmove', preventDefaultThing, false);
				$('.dr-contents .btn')[0].removeEventListener('touchmove', preventDefaultThing, false);
			});
		});
	}
})($);