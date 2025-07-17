/**
 * Created by jiachenpan on 16/11/18.
 */

export function isvalidUsername(str) {
  // const valid_map = ['admin', '客服昵称']
  // return valid_map.indexOf(str.trim()) >= 0
  return str.trim().length>0
}

/* 合法uri*/
export function validateURL(textval) {
  const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return urlregex.test(textval)
}

/* 小写字母*/
export function validateLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/* 大写字母*/
export function validateUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/* 大小写字母*/
export function validatAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/* 是否手机号码或者固话*/
export function validatePhoneTwo(rule, value, callback) {
  const reg = /^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/;;
  if (value == '' || value == undefined || value == null) {
    callback();
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的电话号码或者固话号码'));
    } else {
      callback();
    }
  }
}
/* 是否固话*/
export function validateTelphone(rule, value,callback) {
  const reg =/0\d{2}-\d{7,8}/;
  if(value==''||value==undefined||value==null){
    callback();
  }else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的固话（格式：区号+号码,如010-1234567）'));
    } else {
      callback();
    }
  }
}
/* 是否手机号码*/
export function validatePhone(rule, value,callback) {
  const reg =/^[1][3,4,5,7,8][0-9]{9}$/;
  if(value==''||value==undefined||value==null){
    callback();
  }else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的电话号码'));
    } else {
      callback();
    }
  }
}
/* 是否身份证号码*/
export function validateIdNo(rule, value,callback) {
  const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
  if(value==''||value==undefined||value==null){
    callback();
  }else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的身份证号码'));
    } else {
      callback();
    }
  }
}
/* 是否邮箱*/
export function validateEMail(rule, value,callback) {
  const reg =/^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
  if(value==''||value==undefined||value==null){
    callback();
  }else{
    if (!reg.test(value)){
      callback(new Error('请输入正确的邮箱地址'));
    } else {
      callback();
    }
  }
}

/* 验证账号 */
export function validateUsername(rule, value, callback) {
  if (value.length < 6 || value.length > 20) {
    return callback(new Error('用户名不得小于6个或大于20个字符!'))
  } else {
    callback()
  }
}
 
/* 验证密码 */
export function validatePassword(rule, value, callback) {
  if (value.length < 6) {
    return callback(new Error('密码不能小于6位'))
  } else {
    callback()
  }
}




// export function bankAccountValid(rule, value, callback){
//   const strBin = '10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,94,95,98,99'
//   if (!value) {
//     return callback(new Error('收款账户不能为空'))
//   } else if (!Number.isInteger(+value)) {
//     callback(new Error('银行卡号必须全为数字'))
//   } else if (value.trim().length < 12 || value.trim().length > 19) {
//     callback(new Error('银行卡号长度必须在12到19之间'))
//   } else if (strBin.indexOf(value.substring(0, 2)) === -1) {
//     callback(new Error('银行卡号开头6位不符合规范'))
//   } else {
//     callback()
//   }
// }
export function bankAccountValid(rule, value, callback){
  const strBin = '10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,94,95,98,99'
  // if (!value) {
  //   return callback(new Error('收款账户不能为空'))
  // } else 
  if (!Number.isInteger(+value)) {
    callback(new Error('银行卡号必须全为数字'))
  } else if (value.trim().length < 12 || value.trim().length > 19) {
    callback(new Error('银行卡号长度必须在12到19之间'))
  } else if (strBin.indexOf(value.substring(0, 2)) === -1) {
    callback(new Error('银行卡号开头6位不符合规范'))
  } else {
    callback()
  }
}

