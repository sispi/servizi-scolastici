import { GetProceedingsByServiceId } from '/static/portal/js/services/proceeding.service.js?no-cache';
export function TreeTemplate() {
    var treeTemplate = Vue.component("tree-item", {
        template: `
        <div>
            <li class="list-group-item d-flex justify-content-between align-items-center">
                <span><img :src="item.logo" alt="Logo" style="max-height: 48px; max-width: 48px;" v-if="item.logo"> {{ item.name }}</span>
                <span v-if="!isFolder">
                    <a v-if="item.externalService==true" v-bind:href="'//'+item.link">
                        <span style="margin-right:10px;" data-toggle="tooltip" data-placement="top" title="Vai al link">{{item.link}}</span>
                    </a>
                    <a v-bind:href="'/backOffice/service/service-update?id=' + item.id">
                        <span class="glyphicon glyphicon-pencil" data-toggle="tooltip" data-placement="top" title="Modifica"></span>
                    </a>
                    <a @click="chooseRedirect(item.id, item.name)" style="cursor: pointer;" class="text-primary">
                        <span v-if="item.externalService==false" class="glyphicon glyphicon-plus" data-toggle="tooltip" data-placement="top" title="Aggiungi sottoservizio o procedimento"></span>
                    </a>
                </span>
                <span @click="toggle" v-if="isFolder" style="cursor: pointer;" class="icon">
                    <a v-bind:href="'/backOffice/service/service-update?id=' + item.id">
                        <span class="glyphicon glyphicon-pencil" data-toggle="tooltip" data-placement="top" title="Modifica"></span>
                    </a>
                    <a v-bind:href="'/backOffice/service/service-create?serviceId=' + item.id + '&serviceName=' + item.name">
                        <span class="glyphicon glyphicon-plus" data-toggle="tooltip" data-placement="top" title="Aggiungi sottoservizio"></span>
                    </a>
                    <span class="glyphicon glyphicon-menu-down" data-toggle="tooltip" data-placement="top" title="Estendi/Ritrai"></span>
                </span>
            </li>
            <ul v-show="isOpen" v-if="isFolder" style="list-style-type: none;">
                <tree-item class="item" v-for="(child, index) in item.children" :key="index" :item="child" @make-folder="$emit('make-folder', $event)" @add-item="$emit('add-item', $event)"></tree-item>
            </ul>
        </div>
        `,
        props: {
            item: Object,
        },
        data: function () {
            return {
                isOpen: false,
                isAPOpen: false,
                agencyProceedings: [],
                elementIdSelected: null,
                elementNameSelected: null
            };
        },
        computed: {
            isFolder: function () {
                return this.item.children && this.item.children.length;
            },
        },
        methods: {
            toggle: function () {
                if (this.isFolder) {
                    this.isOpen = !this.isOpen;
                }
            },
            chooseRedirect: function(serviceId, serviceName){
                const res = GetProceedingsByServiceId(serviceId);
                console.log(res);
                if(res.status === 'success'){
                    if(res.data.length > 0){
                        location.href = "/backOffice/proceeding/proceeding-create?serviceId=" + serviceId + "&serviceName=" + serviceName;
                    } else {
                        location.href = "/backOffice/neutral/choose-what-to-create?serviceId=" + serviceId + "&serviceName=" + serviceName;
                    }
                }
            }
        },
    });
    return treeTemplate;
}
