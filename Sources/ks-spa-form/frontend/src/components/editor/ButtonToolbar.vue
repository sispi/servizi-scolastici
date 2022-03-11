<template>
    <div class="button-toolbar">
      <b-card no-body class="card-header p-0 w-100 m-b-20">
        <b-card-body class="p-0" v-if="toolbar != null">
          <span v-for="(button, index) in toolbar.buttons" :key="index">
            <b-button class="m-10 float-left btn-filippetti" :class="'button-' + index">
              <b class="m-r-20">{{button.label}}</b> &nbsp;
              <font-awesome-icon icon="pencil-alt" class="cursor-pointer" v-b-modal.modalButton @click="editButton(index)" /> &nbsp;
              <font-awesome-icon icon="times" @click="removeButton(index)"></font-awesome-icon>
            </b-button>
          </span>
          <b-button variant="default" class="m-10 float-left addButton" v-b-modal.modalButton>
            <font-awesome-icon style="font-size: 19px" icon="plus-circle" />&nbsp;
            <span class="m-r-20"> Aggiungi Bottone</span>
          </b-button>
          <b-button variant="default" class="moveToolbarButton float-right" @click="switchToolbarPosition" v-b-tooltip.hover title="Sposta toolbar sopra/sotto">
            <font-awesome-icon v-show="toolbar.position === 'top'" icon="arrow-down" />
            <font-awesome-icon v-show="toolbar.position === 'bottom'" icon="arrow-up" />
          </b-button>
        </b-card-body>
      </b-card>
    </div>
</template>

<script>
export default {
  name: 'ButtonToolbar',
  props: {
    toolbar: {}
  },
  data () {
    return {
      buttonIndex: null
    }
  },
  methods: {
    switchToolbarPosition () {
      var toolbar = this.toolbar
      if (toolbar.position === 'bottom') {
        toolbar.position = 'top'
      } else {
        toolbar.position = 'bottom'
      }
    },
    editButton (buttonIndex) {
      this.$emit('edit-button', buttonIndex)
    },
    removeButton (index) {
      if (this.toolbar.buttons != null) {
        this.$bvModal.msgBoxConfirm('Sei sicuro di voler eliminare il bottone selezionato?', {
          title: 'Elimina Bottone',
          okTitle: 'Conferma',
          cancelTitle: 'Annulla',
          hideHeaderClose: false
        }).then(value => {
          if (value) {
            this.toolbar.buttons.splice(index, 1)
          }
        }).catch(err => {
          console.log(err)
        })
      }
    }
  }
}
</script>

<style scoped>
  .moveToolbarButton {
    color: #0970b8!important;
    transition: ease-in 0.5s;
    margin-top: 10px;
    margin-right: 5px;
  }

  .moveToolbarButton:hover {
    color: #35495e!important;
  }

  .btn-filippetti {
    transition: ease-in 0.5s;
    background-color: white;
    border-color: #0970b8;
    color: #0970b8;
  }

  .btn-filippetti:hover {
    border-color: #35495e;
    color: #35495e;
  }

  .cursor-pointer {
    cursor: pointer;
  }

  .p-5 {
    padding: 5px!important;
  }

  .m-10 {
    margin: 10px;
  }

  .m-r-20 {
    margin-right: 20px;
  }
</style>
