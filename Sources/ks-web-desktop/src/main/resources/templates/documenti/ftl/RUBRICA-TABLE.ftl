<template v-if="listaAllegati.length>0">
    <table class="table table-striped">
        <thead>
        <tr>
            <th style="width: 25%;">
                <a class="order blk" :href="changeQs('orderBy',model.sortSpecs['DES_RUBRICA'],'pageNumber')">
                    {{messages["listaAnagrafiche.descrizione"] || "Descrizione"}} <i class="bi bi-arrow-down-up mainColor"></i>
                </a>
            </th>
            <th style="width: 10%;text-align: center;">
                <a class="order blk" :href="changeQs('orderBy',model.sortSpecs['TIPO_RUBRICA'],'pageNumber')">
                    {{messages["listaAnagrafiche.tipo"] || "Tipo"}} <i class="bi bi-arrow-down-up mainColor"></i>
                </a>
            </th>
            <th style="width: 10%;text-align: center;">
                <a>
                    {{messages["listaAnagrafiche.cfPivaUfficio"] || "CF/P.Iva/Ufficio"}}
                </a>
            </th>
            <th style="width: 22%;text-align: center;">
                <a>
                    {{messages["listaAnagrafiche.email"] || "Email"}}
                </a>
            </th>
            <th style="width: 23%;text-align: center;">
                <a>
                    {{messages["listaAnagrafiche.indirizzoPostale"] || "Indirizzo Postale"}}
                </a>
            </th>
            <th style="width: 10%;">
            </th>
        </tr>
        </thead>
        <tbody>
            <tr v-for="(doc,i) in listaAllegati" :key="i">
                <td>
                    <span v-if="doc.TIPO_RUBRICA == 'Persona'">
                        <i style="font-size: 16px;" class="bi bi-person-fill"></i>
                    </span>
                    <span v-else-if="doc.TIPO_RUBRICA == 'PersonaGiuridica'">
                        <i class="bi bi-pen-fill"></i>
                    </span>
                    <span v-else-if="doc.TIPO_RUBRICA == 'Amministrazione'">
                        <i class="bi bi-house-fill"></i>
                    </span>
                    <span v-else>
                        <i class="bi bi-journal-bookmark-fill"></i>
                    </span>
                    <a @click="openElementRubrica(i)"  href="#" @click.prevent="showEditanagraficaModal = true"><strong> {{ doc.DES_RUBRICA }}</strong></a>
                </td>
                <td style="text-align: center;">
                    <template v-if="doc.TIPO_RUBRICA == 'Persona'">
                        {{messages["listaAnagrafiche.pf"] || "PF"}}
                    </template>
                    <template v-else-if="doc.TIPO_RUBRICA == 'PersonaGiuridica'">
                        {{messages["listaAnagrafiche.pg"] || "PG"}}
                    </template>
                    <template v-else-if="doc.TIPO_RUBRICA == 'Amministrazione'">
                        {{messages["listaAnagrafiche.pa"] || "PA"}}
                    </template>
                </td>
                <td style="text-align: center;">
                    <template v-if="doc.TIPO_RUBRICA == 'Persona'">
                        {{ doc.CODICE_FISCALE }}    
                    </template>
                    <template v-else-if="doc.TIPO_RUBRICA == 'PersonaGiuridica'">
                        {{ doc.PARTITA_IVA }}   
                    </template>
                    <template v-else-if="doc.TIPO_RUBRICA == 'Amministrazione'">
                        {{ doc.AOO_UFFICIO }}   
                    </template>
                </td>
                <td style="text-align: center;">
                    {{ doc.INDIRIZZO_PEC?doc.INDIRIZZO_PEC:doc.INDIRIZZO_PEO }}   
                </td>
                <td style="text-align: center;">
                    {{ doc.INDIRIZZO_POSTALE }}
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