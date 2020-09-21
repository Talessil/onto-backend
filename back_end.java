package cz.makub;

import org.semanticweb.owlapi.formats.*;
import com.clarkparsia.owlapi.explanation.DefaultExplanationGenerator;
import com.clarkparsia.owlapi.explanation.util.SilentExplanationProgressMonitor;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.google.common.collect.Multimap;
import com.sun.org.apache.xpath.internal.NodeSet;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDisjointClassesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentDataPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentObjectPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredInverseObjectPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredObjectPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubDataPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubObjectPropertyAxiomGenerator;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationOrderer;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationOrdererImpl;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree;
import uk.ac.manchester.cs.owl.explanation.ordering.Tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * Example how to use an OWL ontology with a reasoner.
 * <p>
 * Run in Maven with <code>mvn exec:java -Dexec.mainClass=cz.makub.Tutorial</code>
 *
 * @author Martin Kuba makub@ics.muni.cz
 * 
 * @prefix : <http://www.semanticweb.org/talessil/ontologies/2019/7/untitled-ontology-42>
 */
public class Tutorial {

    private static OWLObjectRenderer renderer = new DLSyntaxObjectRenderer();
   private static final String uri = "http://www.semanticweb.org/talessil/ontologies/2019/7/untitled-ontology-42";

    public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
    	
    	//prepare ontology and reasoner
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File("C:\\Users\\Talessil\\Desktop\\BD\\neto2.owl");
        File file2 = new File("C:\\Users\\Talessil\\Desktop\\BD\\inferred-neto2.owl");
        
        OWLOntology o = manager.loadOntologyFromOntologyDocument(file2);
        OWLReasonerFactory rf = PelletReasonerFactory.getInstance();
        OWLReasoner reasoner = rf.createReasoner(o);
        OWLDataFactory df = manager.getOWLDataFactory();
        PrefixDocumentFormat pm = manager.getOntologyFormat(o).asPrefixOWLOntologyFormat();
        pm.setDefaultPrefix(uri + "#");
        
        
        
      //instanciação de classes
  		/*OWLClass Node = df.getOWLClass("Node", pm);
  		OWLClass Keyword = df.getOWLClass("Keyword", pm);
  		OWLClass Weight = df.getOWLClass("Weight", pm);
  		OWLClass KeywordWeight = df.getOWLClass("KeywordWeight", pm);
  		OWLClass TopicWeight = df.getOWLClass("TopicWeight", pm);
  		OWLSubClassOfAxiom sub = df.getOWLSubClassOfAxiom(KeywordWeight,Weight);
  		manager.addAxiom(o, sub);
  		OWLSubClassOfAxiom sub2 = df.getOWLSubClassOfAxiom(TopicWeight,Weight);
  		manager.addAxiom(o, sub2);
  		OWLDataProperty name = df.getOWLDataProperty("name", pm);
  		OWLDataProperty weight = df.getOWLDataProperty("weight", pm);
  		OWLObjectProperty hasKeyword = df.getOWLObjectProperty("hasKeyword", pm);
  		OWLObjectProperty hasWeight = df.getOWLObjectProperty("hasWeight", pm);
  		OWLObjectProperty hasTopicWeight = df.getOWLObjectProperty("hasTopicWeight", pm);
  		
  		
    	
  		//Carregar indivíduos
			try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Talessil\\Desktop\\BD\\sample.csv"))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			    	String[] pessoas = line.split(";");
			        //System.out.println(pessoas[0] + ";" + pessoas[1] + ";" + pessoas[2]);
			        
			    	//adiciona individuo genérico
			        OWLIndividual node = df.getOWLNamedIndividual(pessoas[0], pm);
			        //adiciona classe Node para o individuo
					OWLClassAssertionAxiom classNode = df.getOWLClassAssertionAxiom(Node, node);
					manager.addAxiom(o, classNode);
					
					///**************************************************************************************
					//adiciona individuo genérico
					OWLIndividual nodeTag = df.getOWLNamedIndividual(pessoas[0]+pessoas[1], pm);
					//adiciona classe KEYWORD para o individuo
					OWLClassAssertionAxiom classKeyword = df.getOWLClassAssertionAxiom(Keyword, nodeTag);
					manager.addAxiom(o, classKeyword);
					
					//adicina DataProperty "name" aos objetos da classe Keyword
					OWLDataPropertyAssertionAxiom DP = df.getOWLDataPropertyAssertionAxiom(name,nodeTag,pessoas[1]);
					manager.addAxiom(o, DP);
					
					//adicionar Object Property "hasKeyword"
					OWLAxiom assertion = df.getOWLObjectPropertyAssertionAxiom(hasKeyword, node, nodeTag);
					manager.addAxiom(o, assertion);
					///**************************************************************************************
					//adiciona individuo genérico
			        OWLIndividual wei = df.getOWLNamedIndividual(pessoas[0]+pessoas[1]+"Weight", pm);
			        //adiciona classe Weight para o individuo
					OWLClassAssertionAxiom classKeywordWeight = df.getOWLClassAssertionAxiom(KeywordWeight, wei);
					manager.addAxiom(o, classKeywordWeight);
					
					//adicina DataProperty "weight" aos objetos da classe Weight
					OWLDataPropertyAssertionAxiom W = df.getOWLDataPropertyAssertionAxiom(weight,wei,pessoas[2]);
					manager.addAxiom(o, W);
					
					//adicionar Object Property "hasWeight"
					OWLAxiom assertion2 = df.getOWLObjectPropertyAssertionAxiom(hasWeight, nodeTag, wei);
					manager.addAxiom(o, assertion2);
					///**************************************************************************************
					//adiciona individuo genérico
			        OWLIndividual topwei = df.getOWLNamedIndividual("TopicWeight"+pessoas[0], pm);
			        //adiciona classe Weight para o individuo
					OWLClassAssertionAxiom classTopicWeight = df.getOWLClassAssertionAxiom(TopicWeight, topwei);
					manager.addAxiom(o, classTopicWeight);
					
					//adicionar Object Property "hasTopicWeight"
					OWLAxiom assertion3 = df.getOWLObjectPropertyAssertionAxiom(hasTopicWeight, node, topwei);
					manager.addAxiom(o, assertion3);
			    }
			}
     		//salvar ontologia
			manager.saveOntology(o, new FunctionalSyntaxDocumentFormat(),new FileOutputStream(file2));
  			*/
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
        
        //set nodes as different nodes
        /*OWLClass cn = df.getOWLClass(":Node", pm);
		Set<OWLIndividual> hs = new HashSet<OWLIndividual>();
		for (OWLNamedIndividual i : reasoner.getInstances(cn, false).getFlattened()) {
			hs.add(i);
		}
		//System.out.println(hs);
		//set indivíduo como distinto dos outros
		OWLDifferentIndividualsAxiom differentNodesAx =  df.getOWLDifferentIndividualsAxiom(hs);
		manager.addAxiom(o, differentNodesAx);
		//---------------------------------------------------------------------------------------
		//set nodes as different nodes
        OWLClass ck = df.getOWLClass(":Keyword", pm);
		Set<OWLIndividual> hs2 = new HashSet<OWLIndividual>();
		for (OWLNamedIndividual i : reasoner.getInstances(ck, false).getFlattened()) {
			hs2.add(i);
		}
		//System.out.println(hs);
		//set indivíduo como distinto dos outros
		OWLDifferentIndividualsAxiom differentNodesAx2 =  df.getOWLDifferentIndividualsAxiom(hs2);
		manager.addAxiom(o, differentNodesAx2);
		//---------------------------------------------------------------------------------------
		//salvar ontologia
		manager.saveOntology(o, new FunctionalSyntaxDocumentFormat(),new FileOutputStream(file2));
		*/
			
		//******************************* SALVAR INFERÊNCIAS NA ONTOLOGIA ********************************
		//prepare ontology and reasoner
        /*manager.addOntologyChangeListener((OWLOntologyChangeListener) reasoner);
        List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<>();
        gens.add(new InferredSubClassAxiomGenerator());  
        gens.add(new InferredClassAssertionAxiomGenerator());
        gens.add( new InferredDisjointClassesAxiomGenerator());
        gens.add( new InferredEquivalentClassAxiomGenerator());
        gens.add( new InferredEquivalentDataPropertiesAxiomGenerator());
        gens.add( new InferredEquivalentObjectPropertyAxiomGenerator());
        gens.add( new InferredInverseObjectPropertiesAxiomGenerator());
        gens.add( new InferredObjectPropertyCharacteristicAxiomGenerator());
        gens.add( new InferredPropertyAssertionGenerator());
        gens.add( new InferredSubDataPropertyAxiomGenerator());
        gens.add( new InferredSubObjectPropertyAxiomGenerator());

        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, gens);
        // OWLOntology infOnt = manager.createOntology();
        iog.fillOntology(df, o);
        OWLXMLDocumentFormat f = new OWLXMLDocumentFormat();
        OutputStream output = new FileOutputStream(new File("C:\\Users\\Talessil\\Desktop\\BD\\inferred-neto2.owl"));
        manager.saveOntology(o, f, output);
        */
		//******************************* SALVAR INFERÊNCIAS NA ONTOLOGIA - FIM *****************************
			
			
		//******************************* JOVAR VALORES DE PESO NA ONTOLOGIA ********************************
        /*int soma = 0;
        OWLClass tkc = df.getOWLClass(":TopicWeight", pm);
        OWLNamedIndividual tw1 = df.getOWLNamedIndividual(":TopicWeightnode1", pm);
        OWLObjectProperty w1 = df.getOWLObjectProperty(":hasWeight", pm);
        OWLDataProperty ww1 = df.getOWLDataProperty(":weight", pm);
        OWLDataProperty ww2 = df.getOWLDataProperty(":totalWeight", pm);
        
        for (OWLNamedIndividual i : reasoner.getInstances(tkc, false).getFlattened()) {
        	soma = 0;
	        for (OWLNamedIndividual ind : reasoner.getObjectPropertyValues(i,w1).getFlattened()) {
	            //System.out.println("TopicWeightnode1 hasWeight: " + renderer.render(ind));
	        	for (OWLLiteral ind2 : reasoner.getDataPropertyValues(ind,ww1)) {
	                //System.out.println(" - DP:weight: " + ind2.getLiteral());
	                soma = soma + Integer.parseInt(ind2.getLiteral());
	            }
	        }
	        System.out.println(i + " Total weight: " + soma);
	        OWLDataPropertyAssertionAxiom W = df.getOWLDataPropertyAssertionAxiom(ww2,i,soma); //DP, ind, valor
			manager.addAxiom(o, W);
        }
      //salvar ontologia
		manager.saveOntology(o, new FunctionalSyntaxDocumentFormat(),new FileOutputStream(file2));
        */
			
		//******************************* JOVAR VALORES DE PESO NA ONTOLOGIA - FIM ***************************
        
			
		//reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY); - a principio nao precisa usar
			
        try {
			o = manager.createOntology();
			System.out.println(o);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
    }
}