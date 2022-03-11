<!-- documento registrato -->
<template v-if="doc.N_REGISTRAZ">
    <span class="green-title">
        <i class="fas fa-registered stampList"></i><strong>{{messages["viewProfile.registrato"] || "Registrato"}}</strong> ({{doc.ID_REGISTRO}}) <strong><span>{{messages["viewProfile.num"] || "N."}}</span> {{doc.N_REGISTRAZ}}</strong> {{messages["viewProfile.il"] || "il"}} <span class="date-row">{{date(doc.D_REGISTRAZ)}}</span>
    </span>
    <template v-if="doc.O_REGISTRAZ">
        <br>
        {{messages["viewProfile.oggetto"] || "Oggetto"}}: {{doc.O_REGISTRAZ}}
    </template>
    <template v-if="doc.P_ANNULL_REGISTRAZ">
        <br>
        <span style="margin-left: 0px;" class="label label-danger-list"> {{messages["viewProfile.registrazioneAnnullata"] || "REGISTRAZIONE ANNULLATA"}}</span>
        {{messages["viewProfile.rif"] || "Rif"}}: <strong>{{doc.P_ANNULL_REGISTRAZ}}</strong> {{messages["viewProfile.del"] || "del"}} {{date(doc.D_ANNULL_REGISTRAZ)}} - {{doc.M_ANNULL_REGISTRAZ}}
    </template>
    <br>
</template>
<!-- documento protocollato -->
<template v-if="doc.NUM_PG">
    <span class="green-title"><i class="fas fa-stamp stampList"></i><strong>{{messages["viewProfile.protocollato"] || "Protocollato"}}</strong> {{messages["viewProfile.in"] || "in"}} {{doc.TIPO_PROTOCOLLAZIONE|verso}} <strong><span>{{messages["viewProfile.num"] || "N."}}</span> {{doc.NUM_PG}}</strong> {{messages["viewProfile.il"] || "il"}} <span class="date-row">{{date(doc.DATA_PG)}}</span></span>
    <template v-if="doc.NUM_PG_EMERGENZA">
        <br>
        <span class="regEmergenzaLista"><i class="fas fa-exclamation red"></i> {{messages["viewProfile.regEmergenza"] || "Registro Emergenza"}} {{messages["viewProfile.num"] || "N."}} {{doc.NUM_PG_EMERGENZA}} {{messages["viewProfile.del"] || "del"}} {{date(doc.DATA_PG_EMERGENZA)}}</span>
    </template>
    <template v-if="doc.OGGETTO_PG">
        <br>
        {{messages["viewProfile.oggetto"] || "Oggetto"}}: {{doc.OGGETTO_PG}}
    </template>
    <template v-if="doc.mittente">
        <br>
        {{messages["viewProfile.mittente"] || "Mittente"}}:
        <strong>
            <template v-if="doc.mittente.tipoCorrispondente">
                <template v-if="doc.mittente.tipoCorrispondente == 'Amministrazione'">
                    <template v-if="doc.mittente.persona">
                        <span class="label-mitt-dest">
                            <i class="fas fa-user-tie small-pad-right"></i>
                            {{doc.mittente.persona.denominazione}} ({{doc.mittente.denominazioneUO}})
                            <template v-if="doc.mittente.denominazioneAmm">
                                <a class="btn-icon" data-toggle="tooltip" data-placement="top" :title="doc.mittente.denominazioneAmm">
                                    <i class="fas fa-info-circle i-icon"></i>
                                </a>
                            </template>
                        </span>
                    </template>
                    <template v-else>
                        <span class="label-mitt-dest">
                            <i class="fas fa-home small-pad-right"></i>
                            <!--{{doc.mittente.denominazioneUO ? doc.mittente.denominazioneUO : doc.mittente.denominazioneAmm}}-->
                            {{doc.mittente.denominazioneUO}}
                            <template v-if="doc.mittente.denominazioneAmm">
                                ({{doc.mittente.denominazioneAmm}})
                            </template>
                            <template v-if="doc.mittente.indirizzoTelematico">
                                <a class="btn-icon" data-toggle="tooltip" data-placement="top" :title="doc.mittente.indirizzoTelematico">
                                    <i class="fas fa-info-circle i-icon"></i>
                                </a>
                            </template>
                            <template v-else-if="(doc.mittente.denominazioneUO)&&(doc.mittente.denominazioneAmm)">
                                <a class="btn-icon" data-toggle="tooltip" data-placement="top" :title="doc.mittente.denominazioneAmm">
                                    <i class="fas fa-info-circle i-icon"></i>
                                </a>
                            </template>
                        </span>
                    </template>
                </template>
                <template v-else>
                    <span class="label-mitt-dest">
                        <i class="fas fa-user small-pad-right"></i>
                        {{doc.mittente.denominazione}}
                        <template v-if="doc.mittente.indirizzoTelematico">
                            <a class="btn-icon" data-toggle="tooltip" data-placement="top" :title="doc.mittente.indirizzoTelematico">
                                <i class="fas fa-info-circle i-icon"></i>
                            </a>
                        </template>
                    </span>
                </template>
            </template>
        </strong>
    </template>

    <template v-if="doc.destinatari">
        <br>
        {{messages["viewProfile.destinatari"] || "Destinatari"}}:
        <strong>
            <template v-for="destinatario in doc.destinatari">
                <template v-if="destinatario.tipoCorrispondente">
                    <template v-if="destinatario.tipoCorrispondente == 'Amministrazione'">
                        <template v-if="destinatario.persona">
                            <span class="label-mitt-dest">
                                <i class="fas fa-user-tie small-pad-right"></i>
                                {{destinatario.persona.denominazione}} ({{destinatario.denominazioneUO}})
                                <template v-if="destinatario.denominazioneAmm">
                                    <a class="btn-icon" data-toggle="tooltip" data-placement="top" :title="destinatario.denominazioneAmm">
                                        <i class="fas fa-info-circle i-icon"></i>
                                    </a>
                                </template>
                            </span>
                        </template>
                        <template v-else>
                            <span class="label-mitt-dest">
                                <i class="fas fa-home small-pad-right"></i>
                                <!--{{destinatario.denominazioneUO ? destinatario.denominazioneUO : destinatario.denominazioneAmm}}-->
                                {{destinatario.denominazioneUO}}
                                <template v-if="destinatario.denominazioneAmm">
                                    ({{destinatario.denominazioneAmm}})
                                </template>
                                <template v-if="destinatario.indirizzoTelematico">
                                    <a class="btn-icon" data-toggle="tooltip" data-placement="top" :title="destinatario.indirizzoTelematico">
                                        <i class="fas fa-info-circle i-icon"></i>
                                    </a>
                                </template>
                                <template v-else-if="(destinatario.denominazioneUO)&&(destinatario.denominazioneAmm)">
                                    <a class="btn-icon" data-toggle="tooltip" data-placement="top" :title="destinatario.denominazioneAmm">
                                        <i class="fas fa-info-circle i-icon"></i>
                                    </a>
                                </template>
                            </span>
                        </template>
                    </template>
                    <template v-else>
                        <span class="label-mitt-dest">
                            <i class="fas fa-user small-pad-right"></i>
                            {{destinatario.denominazione}}
                            <template v-if="destinatario.indirizzoTelematico">
                                <a class="btn-icon" data-toggle="tooltip" data-placement="top" :title="destinatario.indirizzoTelematico">
                                    <i class="fas fa-info-circle i-icon"></i>
                                </a>
                            </template>
                        </span>
                    </template>
                </template>
            </template>
        </strong>
    </template>

    <template v-if="doc.P_ANNULL_PG">
        <br>
        <span style="margin-left: 0px;" class="label label-danger-list"> {{messages["viewProfile.protocollazioneAnnullata"] || "PROTOCOLLAZIONE ANNULLATA"}}</span>
        {{messages["viewProfile.rif"] || "Rif"}}: <strong>{{doc.P_ANNULL_PG}}</strong> {{messages["viewProfile.del"] || "del"}} {{date(doc.D_ANNULL_PG)}} - {{doc.M_ANNULL_PG}}
    </template>
</template>