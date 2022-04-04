package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	//inserisco subito quello che ricevo da input
	private List<Esame> esami;
	private Set<Esame> migliore;//tanto non conta l'ordine
	private double mediaMigliore;
	
	public Model() {
		EsameDAO dao=new EsameDAO();
		this.esami=dao.getTuttiEsami();//lo metto nel costruttore almeno lavoro con il database
		                               // una sola volta
		   
	}
	


	public Set<Esame> calcolaSottoinsiemeEsami(int m) {
		//ripristino soluzione migliore
		migliore=new HashSet<Esame>();
		mediaMigliore=-1;
		
		Set<Esame> parziale=new HashSet<Esame>();
		//cerca1(parziale,0,m);
		cerca2(parziale,0,m);
		
		return migliore;	
	}

	
	private void cerca2(Set<Esame> parziale, int L, int m) {
	//Controllare i casi terminali
		
		int sommaCrediti=sommaCrediti(parziale);
		if(sommaCrediti>m)//soluzione non valida
			return;
		
	    if(sommaCrediti==m) {
	    	//controlliamo se per ora è la migliore
	    	
	    	double mediaVoti=calcolaMedia(parziale);
	    	if(mediaVoti>mediaMigliore) {
	    		migliore= new HashSet(parziale);//faccio una copia di parziale perchè
	    	    mediaMigliore=mediaVoti;        // se facessi migliore=parziale
	    	                                    // andrei a copiare il riferimento solo che parziale evolve continuamente
	    	}
	    	return;
	    }
	    //sicuramente, crediti<m
	    if(L==esami.size())
	    	return;
	    
	    //provo ad aggiungere esami[L]
	    parziale.add(esami.get(L));//Per questo metodo mi serve che tutti gli esami siano in una lista perchè mi permette l'accesso ordinato con  indice
	    cerca2(parziale,L+1,m);
	    		
	    
	    //provo a non aggiungere esami[L]
	    parziale.remove(esami.get(L));
	    cerca2(parziale,L+1,m);
		
	}



	private void cerca1(Set<Esame> parziale, int L, int m) {
	//Controllare i casi terminali
		
		int sommaCrediti=sommaCrediti(parziale);
		if(sommaCrediti>m)//soluzione non valida
			return;
		
	    if(sommaCrediti==m) {
	    	//controlliamo se per ora è la migliore
	    	
	    	double mediaVoti=calcolaMedia(parziale);
	    	if(mediaVoti>mediaMigliore) {
	    		migliore= new HashSet(parziale);//faccio una copia di parziale perchè
	    	    mediaMigliore=mediaVoti;        // se facessi migliore=parziale
	    	                                    // andrei a copiare il riferimento solo che parziale evolve continuamente
	    	}
	    	return;
	    }
	    //sicuramente, crediti<m
	    if(L==esami.size())
	    	return;
	    
	    //generiamo i sotto-problemi
	    for(Esame e: esami) {
	    	if(! parziale.contains(e)) {
	    		parziale.add(e);
	    		cerca1(parziale,L+1,m);
	    		parziale.remove(e);//funziona o con HashSet o Map perchè sto facento il backtracking e mi serve rimuovere l'ultimo inserito invece con le liste potrei avere duplicati e con il contains mi prende solo il primo che becca
	    		                   // avrei dovuto fare il remove con remove(index=parziali.size-1)
	    	}
	    }
	    
	}



	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}