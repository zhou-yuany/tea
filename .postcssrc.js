module.exports = {
  plugins: {
    'postcss-pxtorem': { 
      rootValue(res) {
        return res.file.indexOf('vant') !==-1 ? 37.5 : 75
      },
      propList: ['*'],
      exclude: 'github-markdown'
    }
  }
}