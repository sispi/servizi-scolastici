const OBJECT = '[object Object]'
const ARRAY = '[object Array]'

function accessors (path) {
  return path.replace(/\[(\d+)\]/g, '.$1').replace(/^\./, '').split('.').map(a => ({
    key: !isNaN(a) ? Number(a) : a,
    isArray: !isNaN(a)
  }))
}

function is (t, o) {
  return t === Object.prototype.toString.call(o)
}

export default {
  methods: {
    evaluate (expression, model) {
      try {
        // eslint-disable-next-line no-new-func
        const value = Function(`with(this){return (${expression})}`).call(model)
        return value === undefined ? null : value
      } catch (e) {
        return null
      }
    },

    render (template, model) {
      return template.replace(/{{.*?}}/g, match => {
        return this.evaluate(match.slice(2, -2), model)
      })
    },

    get (path, model, defaultValue) {
      const a = accessors(path)
      let v = model
      for (let i = 0; i < a.length; i++) {
        if (v != null) {
          v = v[a[i].key]
        } else {
          break
        }
      }
      return v == null ? (defaultValue === undefined ? null : defaultValue) : v
    },

    set (path, value, model) {
      const a = accessors(path)
      let c = model
      if ((a[0].isArray && !is(ARRAY, c)) || (!a[0].isArray && !is(OBJECT, c))) {
        return
      }
      for (let i = 0; i < a.length - 1; i++) {
        if (a[i + 1].isArray) {
          if (!is(ARRAY, c[a[i].key])) {
            this.$set(c, a[i].key, [])
          }
        } else if (!is(OBJECT, c[a[i].key])) {
          this.$set(c, a[i].key, {})
        }
        c = c[a[i].key]
      }
      this.$set(c, a[a.length - 1].key, value)
    },

    isPath (string) {
      return string.match(/^((\$item)|([a-zA-Z_][a-zA-Z0-9_]*))(\[\d+\])*(\.[a-zA-Z_][a-zA-Z0-9_]*(\[\d+\])*)*$/)
    },

    getPathHints (schema, forInput, forPath, forType) {
      const self = this
      return (function process (schema, path, hints) {
        const type = self.get('type', schema, 'any')
        const format = type === 'string' ? self.get('format', schema) : null
        const itemsType = type === 'array' ? self.get('items.type', schema) : null
        const itemsFormat = type === 'array' ? self.get('items.format', schema) : null
        const input = self.get('input', schema, true)
        const output = self.get('output', schema, true)
        const _type = `${type}${format ? ':' + format : ''}${itemsType ? '<' + itemsType + (itemsFormat ? ':' + itemsFormat : '') + '>' : ''}`
        const _path = forPath ? path.slice(forPath.length).replace(/^\.|\[0\]\.?/, '') : path
        const _item = forPath ? '$item' + (_path.indexOf('[0]') === 0 ? '' : '.') : ''
        if (
          (_path) &&
          ((!forInput && output) || input) &&
          (!forPath || path.indexOf(`${forPath}.`) === 0 || path.indexOf(`${forPath}[0]`) === 0) &&
          (!forType || RegExp(`^(${forType})$`).test(_type))
        ) {
          hints.push({ id: `${_item}${_path}`, text: `${_item}${_path} (${_type})` })
        }
        if (type === 'object' && schema.properties) {
          for (const [key, value] of Object.entries(schema.properties)) {
            process(value, path ? `${path}.${key}` : key, hints)
          }
        }
        if (type === 'array' && schema.items) {
          process(schema.items, path ? `${path}[0]` : '[0]', hints)
        }
        return hints
      })(schema, '', [])
    },

    getDefinitionHint (schema) {
      return {
        header: null,
        footer: null,
        toolbar: {
          position: 'bottom',
          buttons: [{
            type: 'submit',
            label: 'Submit'
          }]
        },
        tabs: [{
          title: 'Default Tab',
          sections: [{
          }]
        }]
      }
    }
  }
}
