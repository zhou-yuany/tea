function show(message){
  wx.showToast({
    title: message,
    icon: 'none',
    duration: 2000
  })
}

// required 必填 默认为必填
// key 验证的字段
// name 字段的中文名字
// type 字段类型 number/string 默认string
//     number: max min
//     string: max min regexp
// message 正则验证失败的消息
exports.submit = function(object, validateDatas){
  for (const index in validateDatas) {
    let v = validateDatas[index]
    let required = v.required != false
    // 验证必填
    if(object[v.key] == undefined || object[v.key] == null || String(object[v.key]).length == 0){
      if(required){
        show(v.message || `请填写${v.name}`)
        return false;
      }else{
        continue;
      }
    }
    // 验证数字
    if(v.type == 'number'){
      let number = object[v.key]
      // 验证数字
      if(!/^[0-9]+(.[0-9]+)?$/.test(String(number))){
        show(`${v.name}请填写数字`)
        return false;
      }
      // 正数
      if(Number(number) < 0){
        show(`${v.name}请填写正数`)
        return false;
      }
      // 范围
      if(v.max != undefined && v.min != undefined && (Number(number) < v.min || Number(number) > v.max)){
        show(`${v.name}必须大于${v.min}并且小于${v.max}`)
        return false;
      }else if(v.max != undefined && Number(number) > v.max){
        show(`${v.name}必须小于${v.max}`)
        return false;
      }else if(v.min != undefined && Number(number) < v.min){
        show(`${v.name}必须大于${v.min}`)
        return false;
      }else if(v.eq != undefined && Number(number) != v.eq){
        show(`${v.name}必须等于${v.min}`)
        return false;
      }
    }
    // 验证长度
    else{
      let string = object[v.key]
      // 范围
      if(v.max != undefined && v.min != undefined && (String(string).length < v.min || String(string).length > v.max)){
        show(`${v.name}长度必须大于${v.min}并且小于${v.max}`)
        return false;
      }else if(v.max != undefined && String(string).length > v.max){
        show(`${v.name}长度必须小于${v.max}`)
        return false;
      }else if(v.min != undefined && String(string).length < v.min){
        show(`${v.name}长度必须大于${v.min}`)
        return false;
      }else if(v.eq != undefined && String(string).length != v.eq){
        show(`${v.name}长度必须等于${v.eq}`)
        return false;
      }
      // 验证正则
      if(v.regexp && !v.regexp.test(String(string))){
        show(v.message);
        return false;
      }
    }
  }
  return true;
}


