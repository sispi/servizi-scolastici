// https://cli.vuejs.org/config/

process.env.VUE_APP_VERSION = require('./package.json').version
module.exports = {
  lintOnSave: false,
  publicPath: '/form-manager',
  // change build paths to make them maven compatible
  outputDir: 'target/dist/spa',
  assetsDir: 'assets',
  //
  devServer: {
    port: 9000,
    proxy: {
      '^/form-manager/api/': {
        target: 'http://localhost:8080',
        pathRewrite: { '^/form-manager/api/': '/form-manager/api/' },
        changeOrigin: true
      }
    }
  }
}
