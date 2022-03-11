import * as components from '@/lib/components.js'

const ComponentLibrary = {
  install (Vue, options = {}) {
    for (const componentName in components) {
      const component = components[componentName]
      Vue.component(component.name, component)
    }
  }
}

export default ComponentLibrary

if (typeof window !== 'undefined' && window.Vue) {
  window.Vue.use(ComponentLibrary)
} else {
  console.error('KeySuite Form Component Library installation failed')
}
