Vue.component('App', { template: `
<div class="container-fluid">
    <div class="container">
        <h2>Info & FAQ</h2>
        <div class="row">
            <div class="col-md-6">
                <h3>Informazioni sull'utilizzo del portale</h3>
                <p class="text-secondary text-justify">
                    Il Portale del Cittadino rende disponibili gli sportelli virtuali dedicati a cittadini e imrpese per consentire l'accesso rapido e unificato ad un ampio numero di procedimenti amministrativi on-line messi a disposizione
                    dai vari uffici.
                </p>
                <h4 class="font-weight-light">Portale del Cittadino</h4>
                <p class="text-secondary text-justify">
                    In basso trovi le istruzioni su come interagire con il Portale. Se ha domande specifiche alle quali non trovi risposta puoi contattarci utilizzando
                    <a class="underlineHover" href="/public/contactUs">questa form</a>.
                </p>
                <div id="accordionDiv2" class="collapse-div" role="tablist">
                    <div class="collapse-header" id="headingA5">
                        <button data-toggle="collapse" data-target="#accordion5" aria-expanded="false" aria-controls="accordion5">
                            Registrarsi, certificarsi, ...
                        </button>
                    </div>
                    <div id="accordion5" class="collapse" role="tabpanel" aria-labelledby="headingA5" data-parent="#accordionDiv2">
                        <div class="collapse-body">
                            All'interno del Portale dei Servizi online del Comune di Palermo sono disponibili servizi a diverso livello di identificazione dell'utente:
                            <ul>
                                <li>Servizi ad accesso libero <i class="fa fa-lock" aria-hidden="true" style="color: #008758;"></i></li>
                                <li>Servizi che prevedono la registrazione dell'Utente <i class="fa fa-lock" aria-hidden="true" style="color: #ffc107;"></i></li>
                                <li>Servizi che prevedono la certificazione dell'Utente <i class="fa fa-lock" aria-hidden="true" style="color: #dc3545;"></i></li>
                            </ul>
                            <p>
                                La registrazione al Portale è attualmente consentita alle persone fisiche (residenti e non residenti), alle imprese e ai professionisti abilitati ai servizi di intermediazione; si effettua online e si
                                completa con il rilascio delle credenziali di accesso, inviate tramite posta elettronica, e l'attribuzione del profilo di "Utente Registrato".
                            </p>
                            <p>
                                La certificazione dell'Utente è attualmente consentita alle persone fisiche residenti, alle imprese e ai professionisti abilitati ai servizi di intermediazione; prevede il rilascio di un codice di attivazione
                                / P.I.N. <i>(Personal Identification Number)</i>
                                e l'attribuzione del profilo di "Utente Certificato".
                            </p>
                            <div>
                                E' possibile richiedere la consegna del PIN (Personal Identification Number, necessario per l'attivazione delle credenziali per l'accesso certificato al Portale dei Servizi online del Comune di Palermo)
                                secondo le due modalità seguenti:
                                <ul>
                                    <li>Posta Ordinaria</li>
                                    <li>Presso Ufficio</li>
                                </ul>
                            </div>
                            <div class="callout">
                                <div class="callout-title">
                                    <i class="fa fa-info-circle" aria-hidden="true"></i>
                                    <span class="sr-only">Info</span>
                                </div>
                                <p>
                                    Procedure per i Cittadini che ricevono, con le lettere di comunicazione (ad esempio: acconti o saldi relativi alla TARI), il P.I.N. per l'attivazione della propria utenza sul Portale. <br />
                                    <i>
                                        (selezionare il collegamento "accedi" in corrispondenza della propria categoria di utenza, per avere accesso alle informazioni di dettaglio sulle procedure di registrazione ed attivazione)
                                    </i>
                                </p>

                                Utenti NON registrati <a href="#">accedi</a><br />
                                Utenti registrati NON certificati <a href="#">accedi</a><br />
                                Utenti certificati <a href="#">accedi</a>
                            </div>

                            <p>
                                In particolare, per certificare la propria utenza, dopo aver effettuato l'accesso al Portale con le credenziali ricevute, selezionare la voce "Profilo Utente" e inserire le informazioni richieste.
                            </p>
                            <p>
                                Scegliendo l'opzione "Posta Ordinaria" una prima parte del PIN Le sarà inviata all'indirizzo di posta elettronica da Lei indicato mentre la seconda parte del PIN Le sarà recapitata mediante servizio postale
                                ordinario.
                            </p>
                            <p>
                                Scegliendo l'opzione "Presso Ufficio" dovrà recarsi presso una delle postazioni anagrafiche accreditate, di seguito indicati, dove un operatore provvederà ad inviarLe una prima parte del PIN mediante una
                                e-Mail all'indirizzo di posta elettronica da Lei indicato ed a consegnarLe, personalmente, il modulo cartaceo contenente la seconda parte del PIN.
                            </p>
                            <p>
                                La informiamo che la modalità "Posta Ordinaria" comporta una specifica limitazione sull'utilizzo dei servizi. In particolare, non Le sarà possibile accedere ai servizi di certificazione anagrafica. La
                                informiamo, inoltre, che tale limitazione potrà essere eliminata recandosi, anche successivamente, presso una delle postazioni anagrafiche accreditate.
                            </p>
                            <p>
                                Il codice di attivazione / P.I.N. deve essere utilizzato per l'attivazione dell'utenza (solo la prima volta) e l'accesso a tutti i servizi per i quali è previsto.
                            </p>
                            <p>
                                <i>Servizio di prossima attivazione</i><br />
                                Per i cittadini (residenti e non residenti) titolari di una identità digitale rilasciata da una Pubblica Amministrazione (Smart Card), la certificazione è contestuale alla registrazione e si completa con il
                                rilascio delle credenziali di accesso e codice di attivazione / P.I.N., inviati tramite posta elettronica, e l'attribuzione del profilo di "Utente Certificato".
                            </p>
                        </div>
                    </div>
                    <div class="collapse-header" id="headingA6">
                        <button data-toggle="collapse" data-target="#accordion6" aria-expanded="false" aria-controls="accordion6">
                            Uffici presso i quali è possibile effettuare la procedura di certificazione
                        </button>
                    </div>
                    <div id="accordion6" class="collapse" role="tabpanel" aria-labelledby="headingA6" data-parent="#accordionDiv2">
                        <div class="collapse-body">
                            <ul>
                                <li>
                                    <strong>Boccadifalco</strong><br />
                                    Piazza Pietro Micca, 26<br />
                                    Tel. 091/6680854 - Fax 091/6680371
                                </li>
                                <li>
                                    <strong>Borgo Nuovo</strong><br />
                                    Largo Pozzillo, 7<br />
                                    Tel. 091/223331 - Fax 091/223002
                                </li>
                                <li>
                                    <strong>Capinere</strong><br />
                                    Via della Capinera, 2<br />
                                    Tel./Fax 091/6474320
                                </li>
                                <li>
                                    <strong>Cuba / Calatafimi</strong><br />
                                    Via Termini Imerese, 6<br />
                                    Tel. 091/405436  Tel./Fax 091/400009  Tel./Fax 091/405468
                                </li>
                                <li>
                                    <strong>Liberà</strong><br />
                                    Via della Libertà, 47<br />
                                    Tel. 091/333471 - Fax 091/323857
                                </li>
                                <li>
                                    <strong>Mezzomonreale / Villatasca</strong><br />
                                    Viale Regione Siciliana, 95<br />
                                    Tel. 091/6454425  091/7409500 - Fax 091/590352
                                </li>
                                <li>
                                    <strong>Noce / Malaspina</strong><br />
                                    Via Bevignani, 74<br />
                                    Tel. 091/6815361 - Fax 091/6815458 - Fax 091/225007
                                </li>
                                <li>
                                    <strong>Oreto Stazione</strong><br />
                                    Corso dei Mille, 203<br />
                                    Tel. 091/6168220 - Fax 091/6161215
                                </li>
                                <li>
                                    <strong>Padre Spoto</strong><br />
                                    Via Padre Spoto<br />
                                    Tel. 091/6216828 - Fax 091/6212831
                                </li>
                                <li>
                                    <strong>Pallavicino</strong><br />
                                    Via Eleonora Duse, 31<br />
                                    Tel. 091/6711054 - Fax 091/6710149
                                </li>
                                <li>
                                    <strong>Partanna Mondello</strong><br />
                                    Piazzetta della Serenità, 5<br />
                                    Tel. 091/6841373  091/452255 - Fax 091/6842633
                                </li>
                                <li>
                                    <strong>Piazza Marina</strong><br />
                                    Piazza Marina<br />
                                    Fax 091/6166387
                                </li>
                                <li>
                                    <strong>Politeama-Montepellegrino</strong><br />
                                    Via Fileti, 19<br />
                                    Tel 091/7407439 - Fax 091/7407437
                                </li>
                                <li>
                                    <strong>Resuttana S.Lorenzo</strong><br />
                                    Via Monte S.Calogero, 28<br />
                                    Tel. 091/7407698- Tel. 091/7407691 - Fax 091/7407672
                                </li>
                                <li>
                                    <strong>S. Giovanni Apostolo</strong><br />
                                    Via Paladini<br />
                                    Tel. 091/6742069 - Fax 091/6741175
                                </li>
                                <li>
                                    <strong>Settecannoli Brancaccio</strong><br />
                                    Via S. Ciro, 15<br />
                                    Tel. 091/6301057 - Fax 091/6302833
                                </li>
                                <li>
                                    <strong>Tommaso Natale/ Sferracavallo</strong><br />
                                    Via Sferracavallo, 146/A<br />
                                    Tel. 091/530630 - Fax 091/530265
                                </li>
                                <li>
                                    <strong>Tricomi</strong><br />
                                    Via Tricomi<br />
                                    Tel./Fax 091/593314
                                </li>
                                <li>
                                    <strong>Uditore / Passo Di Rigano</strong><br />
                                    Via Adua, 22<br />
                                    Tel. 091/552973 - Tel./Fax 091/400314
                                </li>
                                <li>
                                    <strong>Villagrazia/Falsomiele</strong><br />
                                    Via Villagrazia, 302<br />
                                    Tel. 091/6631901 - Fax 091/430119
                                </li>
                                <li>
                                    <strong>Zisa</strong><br />
                                    Via Giuseppe Savagnone, 5/7<br />
                                    Tel. 091/226705  091/226671 - Fax 091/225927
                                </li>
                            </ul>
                            <div>
                                Altri uffici:
                                <ul>
                                    <li>
                                        <strong>Anagrafe</strong><br />
                                        Viale Lazio, 119A<br />
                                        Tel. 091/7405200 - Fax 091/7405224
                                    </li>
                                    <li>
                                        <strong>Stato Civile</strong><br />
                                        Piazza Giulio Cesare, 52
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <h3>FAQ</h3>
                <div id="accordionDiv1" class="collapse-div" role="tablist">
                    <div class="collapse-header" id="headingA1">
                        <button data-toggle="collapse" data-target="#accordion1" aria-expanded="true" aria-controls="accordion1">
                            Cosa si può fare sul Portale dei Servizi online del Comune di Palermo?
                        </button>
                    </div>
                    <div id="accordion1" class="collapse show" role="tabpanel" aria-labelledby="headingA1" data-parent="#accordionDiv1">
                        <div class="collapse-body">
                            Sul Portale dei Servizi online del Comune di Palermo tutti gli utenti possono accedere alle informazioni presenti nel Portale ed usufruire dei servizi ad "accesso libero". Potranno, inoltre, usufruire di
                            specifici servizi online, coloro che, seguendo le procedure indicate nella pagina "Informazioni", accessible dal menù posto in alto a destra nella home page, avranno acquisito le credenziali richieste.
                        </div>
                    </div>
                    <div class="collapse-header" id="headingA2">
                        <button data-toggle="collapse" data-target="#accordion2" aria-expanded="false" aria-controls="accordion2">
                            Chi si può identificare al Portale dei Servizi online del Comune di Palermo?
                        </button>
                    </div>
                    <div id="accordion2" class="collapse" role="tabpanel" aria-labelledby="headingA2" data-parent="#accordionDiv1">
                        <div class="collapse-body">
                            Attualmente si possono identificare solo le persone fisiche, maggiorenni, titolari di un codice fiscale italiano ed in possesso di un indirizzo di posta elettronica.
                        </div>
                    </div>
                    <div class="collapse-header" id="headingA3">
                        <button data-toggle="collapse" data-target="#accordion3" aria-expanded="false" aria-controls="accordion3">
                            Cosa si può fare come utente "Certificato"?
                        </button>
                    </div>
                    <div id="accordion3" class="collapse" role="tabpanel" aria-labelledby="headingA3" data-parent="#accordionDiv1">
                        <div class="collapse-body">
                            L'utente viene identificato dall'Amministrazione tramite una procedura di verifica dell'identità personale. Con questo profilo si può accedere, a banche dati e servizi di tipo personale ed effettuare, ad esempio,
                            la visura della propria posizione anagrafica o tributaria.
                        </div>
                    </div>
                    <div class="collapse-header" id="headingA3">
                        <button data-toggle="collapse" data-target="#accordion4" aria-expanded="false" aria-controls="accordion4">
                            Come ci si identifica sul Portale dei Servizi online del Comune di Palermo?
                        </button>
                    </div>
                    <div id="accordion4" class="collapse" role="tabpanel" aria-labelledby="headingA4" data-parent="#accordionDiv1">
                        <div class="collapse-body">
                            Per i possessori di un'identità digitale (Smart Card) il rilascio delle credenziali di accesso come utente "certificato" del Portale dei Servizi online del Comune di Palermo è immediato. Per chi non dispone di
                            questi dispositivi di identificazione ed accesso la procedura prevede due fasi:
                            <ul>
                                <li>la registrazione, che viene completata online;</li>
                                <li>
                                    l'identificazione e certificazione, che si conclude dopo la presentazione del Contratto di adesione al servizio di "Identificazione e Certificazione Utente per l'accesso al Portale dei Servizi online del
                                    Comune di Palermo" (<a href="https://servizionline.comune.palermo.it/portcitt/docs/comupa_facsimile_contratto_adesione.pdf" target="_blank">facsimile</a>) sottoscritto dal richiedente, insieme ad una
                                    copia di un documento di identità, presso presso uno degli uffici dell'Amministrazione abilitati.
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
`
}); 
new Vue({ el: "#app" });
