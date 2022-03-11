package keysuite.docer.client.corrispondenti;

public interface IAmministrazione extends ICorrispondente {

    String getCodiceAmm();
    String getDenominazioneAmm();

    String getCodiceAOO();
    String getDenominazioneAOO();

    String getCodiceUO();
    String getDenominazioneUO();

    IPersona getPersona();

    boolean isCompetente();

}
