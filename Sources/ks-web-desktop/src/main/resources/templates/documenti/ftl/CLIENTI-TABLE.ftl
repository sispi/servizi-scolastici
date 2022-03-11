<template v-if="listaAllegati.length>0">
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 25%;">
                <a class="order blk" :href="changeQs('orderBy',model.sortSpecs['DES_CLIENTI'],'pageNumber')">
                    {{messages["listaAnagrafiche.descrizione"] || "Descrizione"}} <i class="bi bi-arrow-down-up mainColor"></i>
                </a>
            </th>
            <th style="width: 15%;text-align: center;">
                <a class="order blk" :href="changeQs('orderBy',model.sortSpecs['COD_CLIENTI'],'pageNumber')">
                    {{messages["listaAnagrafiche.codice"] || "Codice"}} <i class="bi bi-arrow-down-up mainColor"></i>
                </a>
            </th>
            <th style="width: 10%;text-align: center;">
                <a class="order blk" :href="changeQs('orderBy',model.sortSpecs['TIPO_CLIENTI'],'pageNumber')">
                    {{messages["listaAnagrafiche.tipo"] || "Tipo"}} <i class="bi bi-arrow-down-up mainColor"></i>
                </a>
            </th>
            <th style="width: 20%;text-align: center;">
                <a class="order blk" :href="changeQs('orderBy',model.sortSpecs['CREATOR'],'pageNumber')">
                    {{messages["listaAnagrafiche.creatoDa"] || "Creato da"}} <i class="bi bi-arrow-down-up mainColor"></i>
                </a>
            </th>
            <th style="width: 10%;text-align: center;">
                {{messages["listaAnagrafiche.metadato1"] || "Metadato 1"}}
            </th>
            <th style="width: 10%;text-align: center;">
                {{messages["listaAnagrafiche.metadato2"] || "Metadato 2"}}
            </th>
            <th style="width: 10%;">
            </th>
        </tr>
        </thead>
        <tbody>
            <tr v-for="(doc,i) in listaAllegati" :key="i">
                <td>
                    <span>
                        <i class="bi bi-journal-bookmark-fill"></i>
                    </span>
                    <a @click="openElementRubrica(i)" href="#" @click.prevent="showEditanagraficaModal = true"><strong> {{ doc.DES_CLIENTI }}</strong></a>
                </td>
                <td style="text-align: center;">
                    {{ doc.COD_CLIENTI }}
                </td>
                <td style="text-align: center;">
                    {{ doc.TYPE_ID }}
                </td>
                <td style="text-align: center;">
                    {{doc.CREATOR}}
                </td>
                <td style="text-align: center;">
                    {{doc.METADATO_1}}
                </td>
                <td style="text-align: center;">
                    {{doc.METADATO_2}}
                </td>
                <td style="text-align: center;">
                    <button @click="openElementRubrica(i)" class="btn-alpha" href="#" @click.prevent="showEditanagraficaModal = true" data-toggle="tooltip" data-placement="top" :title=" messages['listaAnagrafiche.modifica']||'Modifica'" >
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button @click="confirmDelete(i)" class="btn-alpha" data-toggle="tooltip" data-placement="top" :title=" messages['listaAnagrafiche.elimina']||'Elimina'" :disabled="listaAllegati[i].permissions.indexOf('destroy')==-1" >
                        <i class="bi bi-x-circle red"></i>
                    </button>
                </td>
            </tr>
        </tbody>
    </table>
</template>
<template v-else>
    <div class="noElementTable">
        <span>{{messages["listaAnagrafiche.nessunElementoPresente"] || "Nessun elemento presente"}}</span>
    </div>
</template>