<span v-if="estensione(doc.DOCNAME) == 'pdf'">
    <i class="far fa-file-pdf icon-lista"></i>
</span>
<span v-else-if="estensione(doc.DOCNAME) == 'doc' || estensione(doc.DOCNAME) == 'ocx'">
    <i class="far fa-file-word icon-lista"></i>
</span>
<span v-else-if="estensione(doc.DOCNAME) == 'xls'">
    <i class="far fa-file-excel icon-lista"></i>
</span>
<span v-else-if="estensione(doc.DOCNAME) == 'txt'">
    <i class="far fa-file-alt icon-lista"></i>
</span>
<span v-else-if="estensione(doc.DOCNAME) == 'p7m' || estensione(doc.DOCNAME) == 'eml'">
    <i class="far fa-envelope-open icon-lista"></i>
</span>
<span v-else>
    <i class="far fa-file-code icon-lista"></i>
</span>