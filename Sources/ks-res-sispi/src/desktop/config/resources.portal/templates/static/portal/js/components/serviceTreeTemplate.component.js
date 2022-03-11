import { GetProceedingsByServiceId } from '/static/portal/js/services/proceeding.service.js?no-cache';
export function TreeTemplate() {
    var treeTemplate = Vue.component("tree-item", {
        template: `
        <li>
            <span v-if="item.valid">
                <a v-if="item.externalService==true" v-bind:href="'//'+item.link" target="_blank">
                    <div class="avatar size-lg">
                        <img :src="item.logo" alt="Logo" style="max-height: 48px; max-width: 48px;" v-if="item.logo">
                    </div>
                    <div class="it-right-zone">
                        <span style="margin-left: 50px;" class="text"><i style="font-size: 15px;" class="far fa-play-circle"></i> {{ item.name }}</span>
                    </div>
                </a>
                <a v-else>
                    <div class="avatar size-lg">
                        <img :src="item.logo" alt="Logo" style="max-height: 48px; max-width: 48px;" v-if="item.logo">
                    </div>
                    <div @click="toggle" style="cursor: pointer;" class="it-right-zone">
                        <span style="width:100%" @click="toggleAP(item.id)" class="text">{{ item.name }}</span>
                        <span v-if="isFolder" class="icon">
                            <i class="fa fa-chevron-down" aria-hidden="true"></i>
                        </span>
                        <span @click="toggleAP(item.id)" v-if="!isFolder" style="cursor: pointer;" class="icon">
                            <i class="fa fa-chevron-down" aria-hidden="true"></i>
                        </span>
                    </div>
                </a>
            </span>
            
            <div class="it-list-wrapper">
                <ul class="link-sublist" v-show="isAPOpen" v-if="agencyProceedings.length > 0" style="list-style-type: none;">
                    <li v-for="agencyProceeding in agencyProceedings" v-if="agencyProceeding.isActive">
                        <a v-bind:href="'/public/proceedingDetail?id=' + agencyProceeding.id">
                            <div class="it-right-zone">
                                <span style="margin-left: 50px;" class="text"> <i style="font-size: 15px;" class="far fa-play-circle"></i> {{ agencyProceeding.title }}</span>
                                <span v-if="agencyProceeding.isActive" class="float-right"><i style="color:#008758;margin-right:18px;" class="fa fa-thumbs-up" aria-hidden="true" title="Procedimento avviabile"></i></span>
                                <span v-else class="float-right"><i style="color:#d9364f;margin-right:18px;" class="fa fa-hand-paper" aria-hidden="true" title="Procedimento non avviabile"></i></span>
                            </div>
                        </a>
                    </li>
                </ul>
            </div>
            <div class="it-list-wrapper">
                <ul class="link-sublist" v-show="isOpen" v-if="isFolder" style="list-style-type: none;">
                    <tree-item class="item" v-for="(child, index) in item.children" :key="index" :item="child" @make-folder="$emit('make-folder', $event)" @add-item="$emit('add-item', $event)"></tree-item>
                </ul>
            </div>
        </li>
        `,
        props: {
            item: Object,
        },
        data: function () {
            return {
                isOpen: false,
                isAPOpen: false,
                agencyProceedings: [],
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
            makeFolder: function () {
                if (!this.isFolder) {
                    this.$emit("make-folder", this.item);
                    this.isOpen = true;
                }
            },
            toggleAP: function (agencyServiceId) {
                this.isAPOpen = !this.isAPOpen;
                if (this.isAPOpen) {
                    const res = GetProceedingsByServiceId(agencyServiceId);
                    if(res.status === 'success'){
                        this.agencyProceedings = res.data;
                    } else {
                        this.agencyProceedings = [];
                    }

                }
            },
        },
    });
    return treeTemplate;
}
