Date.prototype.Format = function(fmt) {
    let o = {
      "M+": this.getMonth() + 1, //月份
      "d+": this.getDate(), //日
      "h+": this.getHours(), //小时
      "m+": this.getMinutes(), //分
      "s+": this.getSeconds(), //秒
      "q+": Math.floor((this.getMonth() + 3) / 3), //季度
      S: this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt))
      fmt = fmt.replace(
        RegExp.$1,
        (this.getFullYear() + "").substr(4 - RegExp.$1.length)
      );
    for (let k in o)
      if (new RegExp("(" + k + ")").test(fmt))
        fmt = fmt.replace(
          RegExp.$1,
          RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length)
        );
    return fmt;
  };
   
  export function formatTimeToStr(times, pattern) {
    let d = new Date(times).Format("yyyy-MM-dd hh:mm:ss");
    if (pattern) {
      d = new Date(times).Format(pattern);
    }
    return d.toLocaleString();
  }
   
  export function fomatDateToStr(date, pattern) {
    let d = date.Format("yyyy-MM-dd hh:mm:ss");
    if (pattern) {
      d = date.Format(pattern);
    }
    return d.toLocaleString();
  }
   
  export function fomatDateToStrToYMD(date, pattern) {
    let d = date.Format("yyyy-MM-dd");
    if (pattern) {
      d = date.Format(pattern);
    }
    return d.toLocaleString();
  }
   
  export function getDay(day) {
    let today = new Date();
    let targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
    today.setTime(targetday_milliseconds); //注意，这行是关键代码
    let tYear = today.getFullYear();
    let tMonth = today.getMonth();
    let tDate = today.getDate();
    let tHour = today.getHours() > 9 ? today.getHours() : "0" + today.getHours();
    let tSecond =
      today.getSeconds() > 9 ? today.getSeconds() : "0" + today.getSeconds();
    let tminute =
      today.getMinutes() > 9 ? today.getMinutes() : "0" + today.getMinutes();
    tMonth = doHandleMonth(tMonth + 1);
    tDate = doHandleMonth(tDate);
    return (
      tYear +
      "-" +
      tMonth +
      "-" +
      tDate +
      " " +
      tHour +
      ":" +
      tminute +
      ":" +
      tSecond
    );
  }
  function doHandleMonth(month) {
    let m = month;
    if (month.toString().length == 1) {
      m = "0" + month;
    }
    return m;
  }
  //获取当天时间00：00:00 ~23:59:59
  export function getCurrDayTime(day, flag) {
    let today = new Date();
    let targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
    today.setTime(targetday_milliseconds); //注意，这行是关键代码
    let tYear = today.getFullYear();
    let tMonth = today.getMonth();
    tMonth = doHandleMonth(tMonth + 1);
    let tDate = today.getDate();
    let time = "";
    if (flag == "startTime") {
      time = tYear + "-" + tMonth + "-" + tDate + " 00:00:00";
    } else {
      time = tYear + "-" + tMonth + "-" + tDate + " 23:59:59";
    }
    return formatTimeToStr(time, "");
  }
   
  export function getMonthDays(year, month) {
    let stratDate = new Date(year, month - 1, 1),
      endData = new Date(year, month, 1);
    let days = (endData - stratDate) / (1000 * 60 * 60 * 24);
    return days;
  }