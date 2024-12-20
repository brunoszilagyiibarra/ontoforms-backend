package uy.com.fing.ontoforms.dataproviders.descriptors;

import org.apache.jena.riot.Lang;
import uy.com.fing.ontoforms.dataproviders.OntologyMetrics;
import uy.com.fing.ontoforms.dataproviders.OntologyTestDescriptor;

import java.util.List;

public class PercepcionArteOntologyDescriptor implements OntologyTestDescriptor {


    public static final String PERCEPCION_ARTE_SUBPERCEPCIONES_RDF = "percepcionArte_subpercepciones.rdf";

    @Override
    public List<String> getDatatypeProperties() {
        return List.of("http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#dimensión",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#alto",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#ancho",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#largo",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#fecha_comienzo",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#fecha_creación",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#imagen",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneEnergía",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneEnergíaElectrica",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneEnergíaVital");
    }

    @Override
    public List<String> getObjectProperties() {
        return List.of(
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#esAutorDe",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#realizadaEn",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneAutor",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneCurador",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneExposición",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneMedioProduccion",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneMedioDeProducciónQuirográfico",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneHerramientaDeDibujo",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneBarra",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePincél",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePluma",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePuntaMetálica",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneTécnicaEscultura",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneTécnicaGrabado",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneTécnicaPintura",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePigmentoDisueltoEnAgua",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePigmentoNoDisueltoEnAgua",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneMedioDeProducciónTecnográfico",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneMedioDeProducciónTecnográficoAnalógico",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneMedioDeProducciónTecnográficoDigital",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneMovimiento",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneNombre",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneNombreAutor",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneObraObraClasificación",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepción",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepcionAuditiva",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneAltura",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneDuración",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneIntensidad",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneRuido",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneTimbre",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepcionDeMovimiento",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneMovimientoAparente",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneMovimientoReal",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneMovimientoRealEspectador",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneMovimientoRealObjeto",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepcionGustativa",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneGustoAmargo",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneGustoDulce",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneGustoSalado",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneGustoÁcido",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepcionOlfativa",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepcionTactil",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneContacto",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneDolor",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneForma",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePresión",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePresiónObjeto",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePresiónPeso",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneTamaño",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneTemperatura",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepcionVisual",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneCromatismo",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneIconografia",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepciónVisualAplicadaAEspacios",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneContenidoImagenFija",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneContenidoImagenMovimiento",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneContenidoObjeto",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneContenidoBajoRelieve",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneContenidoBulto",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneContenidoOtrosObjetos",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneContenidoSeresVivos",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepciónVisualAplicadaAImagenEnMovimiento",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepciónVisualAplicadaAImagenFija",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepciónVisualAplicadaAObjetos",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepciónVisualAplicadaABajoRelieve",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepciónVisualAplicadaABulto",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepciónVisualAplicadaAOtrosObjetos",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepciónVisualAplicadaASeresVivos",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepciónVisualAplicadaAAnimales",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepciónVisualAplicadaAHumanos",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePercepciónVisualAplicadaAVegetales",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePieza",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePiezaExposición",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporte",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteCine",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteFormatoCine",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteFotografíaAnalógica",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteImpresión",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteNegativos",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteFotografíaDigital",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteMadera",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteMetal",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoportePiedra",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneMetamórficas",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tienePlutónica",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSedimentaria",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneVolcánica",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoportePlástico",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteTermoestable",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteTermoplástico",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteVideoAnalógico",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneSoporteVideoDigital",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#tieneTítulo"

        );
    }


    @Override
    public List<String> getFirstLevelClasses() {
        return List.of(
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#Ciudad",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#Exposición",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#Familia", //Falla por ser sub-class de PiezaExposición.
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#MedioDeProducción",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#NombreAutor",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#NombreExposición",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#Percepción",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#Persona",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#Pieza",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#PiezaExposición", //Falla por ser sub-class de Familia.
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#Soporte",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#TipoDeMedioDeProducción",
                "http://www.semanticweb.org/usuario/ontologies/2015/8/percepcion_arte#TítuloPieza"
        );
    }

    @Override
    public List<String> getIndividualsByClassUri(String classUri) {
        return null;
    }

    @Override
    public String getOntologyFilePath() {
        return "ontologies/percepcionArte_subpercepciones.rdf";
    }

    @Override
    public String getOntologyFileName() {
        return PERCEPCION_ARTE_SUBPERCEPCIONES_RDF;
    }

    @Override
    public String toString() {
        return getOntologyFileName();
    }

    @Override
    public Lang getLang() {
        return Lang.RDFXML;
    }

    @Override
    public OntologyMetrics getOntologyStats() {
        return OntologyMetrics.builder()
                .classCount(158)
                .objectPropertyCount(94) //Protegé marca 95 porque considera topObjectProperty
                .dataPropertyCount(10)
                .individualCount(384)
                .build();
    }
}