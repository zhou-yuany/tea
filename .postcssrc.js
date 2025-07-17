module.exports = {
  plugins: {
    'postcss-pxtorem': { 
      rootValue(res) {
        return res.file.indexOf('vant') !==-1 ? 37.5 : 192
      },
      propList: ['*'],
      exclude: 'github-markdown'
    }
  }
}