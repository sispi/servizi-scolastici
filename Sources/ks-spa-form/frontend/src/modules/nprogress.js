/* eslint-disable no-undef */
import NProgress from 'nprogress'
import 'js-loading-overlay'

NProgress.doStart = NProgress.start
NProgress.doDone = NProgress.done
NProgress.clearDelay = function () {
  if (this.startDelay) {
    clearTimeout(this.startDelay)
    this.startDelay = undefined
  }
}
NProgress.start = function () {
  this.clearDelay()
  this.startDelay = setTimeout(function () {
    NProgress.doStart()
    JsLoadingOverlay.show()
  }, this.settings.delay || 0)
}
NProgress.done = function () {
  this.clearDelay()
  this.doDone()
  JsLoadingOverlay.hide()
}
NProgress.configure({
  showSpinner: false,
  delay: 500
})
JsLoadingOverlay.setOptions({
  overlayBackgroundColor: '#FFFFFF',
  overlayOpacity: 0.5,
  overlayZIndex: 900,
  spinnerColor: 'transparent'
})

export default NProgress
