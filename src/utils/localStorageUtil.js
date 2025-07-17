import _ from 'lodash'
import moment from 'moment'
export default {
  /**
   * 获取session-storage 中的值
   * @param {*} key
   * @param {*} defaultValue
   */
  get(key, defaultValue) {
    return this.parse(key, defaultValue)
  },
  /**
   * 放入 session-storage 中，自动字符串化 obj
   * @param {*} key
   * @param {*} obj
   * @param {Integer} expires 过期时间：天
   */
  set(key, obj, expires) {
    if (expires) {
      const tmpTime = moment()
        .add(expires, 'days')
        .format('YYYY-MM-DD')
      const handleObj = { expires: tmpTime, value: obj }
      localStorage.setItem(key, JSON.stringify(handleObj))
    } else {
      if (_.isObject(obj)) {
        localStorage.setItem(key, JSON.stringify(obj))
      } else {
        localStorage.setItem(key, obj)
      }
    }
  },
  /**
   * 从 session-storage 中移除key
   * @param {*} key
   */
  remove(key) {
    localStorage.removeItem(key)
  },

  /**
   * 从 session-storage 取出key并将值对象化
   * @param {*} key
   * @param {*} defaultValue
   */
  parse(key, defaultValue) {
    let value = localStorage.getItem(key)
    if (_.isObject(value)) {
      const valueObj = JSON.parse(value)
      if (valueObj.expires) {
        // 有过期时间，判断是否过期：在现在时间之前，过期
        if (moment(valueObj.expires).isBefore(moment(), 'day')) {
          // 删除
          this.remove(key)
          // 直接返回
          return null
        }
        return valueObj.value
      }
      // 没有过期时间直接返回对象
      return valueObj
    }
    // 不是对象，返回值
    return value || defaultValue
  }
}
