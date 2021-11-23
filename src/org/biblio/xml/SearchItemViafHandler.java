package org.biblio.xml;

import java.util.ArrayList;
import java.util.List;
import org.biblio.mapping.viaf.CoAuthors;
import org.biblio.mapping.viaf.Countries;
import org.biblio.mapping.viaf.LanguageOfEntity;
import org.biblio.mapping.viaf.NationalityOfEntity;
import org.biblio.mapping.viaf.Publishers;
import org.biblio.mapping.viaf.Titre;
import org.biblio.mapping.viaf.Viaf;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class SearchItemViafHandler  extends DefaultHandler{
    private final String RESPONSE="searchRetrieveResponse";
    private final String VIADID="ns2:VIAFID";
    private final String TITLES="ns2:titles";
    private final String DATA = "ns2:DATA";
    private final String TEXT = "NS2:TEXT";
    private final String SOURCES = "ns2:sources";
    private final String LIBRARY ="ns2:source";
    private final String SOURCE = "ns2:S";
    private final String NAMETYPE ="ns2:nameType";
    private final String COAUTHORS="ns2:coauthors";
    private final String PUBLISHERS="ns2:publishers";
    private final String COUNTRIES = "ns2:countries";
    private final String LANGUAGEOFENTITY = "ns2:languageOfEntity";
    private final String BIRTHDATE ="ns2:birthDate";
    private final String DATETYPE ="ns2:dateType";
    private final String DEATHDATE = "ns2:deathDate";
    private final String NATIONALITYENTITY = "ns2:nationalityOfEntity";
 
    boolean bfResponse = false;
    boolean bfViafId = false ;
    boolean bfTitles = false ;
    boolean bfData = false ;
    boolean bfText = false ;
    boolean bfSource = false ;
    boolean bfLibrary = false;
    boolean bfcoAuthors = false ;
    boolean bfPublishers = false ;
    boolean bfCountries = false ;
    boolean bfLanguageOfEntity = false;
    boolean bfBirthDate = false;
    boolean bfDateType  = false ;
    boolean bfDeathDate = false ;
    boolean bfNationalityEntity = false ;
 
    private Viaf viaf;
    private Titre dataTitle ;
    private CoAuthors dataCoAuthor;
    private Publishers dataPublishers;
    private Countries dataCountry ;
    private LanguageOfEntity dataLanguageEntity;
    private NationalityOfEntity dataNationality;
 
    private List<String> libraries ;
    private List<String> sourcesData;
    private List<Titre> titres ;
    private List<CoAuthors> coAuthors ;
    private List<Publishers> publishers;
    private List<Countries> countries ;
    private List<LanguageOfEntity> languageOfEntities;
    private List<NationalityOfEntity> nationalityEnties;
 
    public void startElement(String uri, String localName,String qName,
            Attributes attributes) throws SAXException {
 
        if(qName.equalsIgnoreCase(VIADID)){
            viaf = new Viaf();
            bfViafId = true;
        }
        if(qName.equalsIgnoreCase(BIRTHDATE)){
            bfBirthDate = true;
        }
        if(qName.equalsIgnoreCase(DATETYPE)){
            bfDateType = true ;
        }
        if(qName.equalsIgnoreCase(DEATHDATE)){
            bfDeathDate = true ;
        }
 
        //Data
        if(qName.equalsIgnoreCase(TITLES)){
            titres = new ArrayList<Titre>();
            bfTitles = true;
        }
        if(qName.equalsIgnoreCase(COAUTHORS)){
            coAuthors = new ArrayList<CoAuthors>();
            bfcoAuthors = true ;
        }
        if(qName.equalsIgnoreCase(PUBLISHERS)){
            publishers = new ArrayList<Publishers>();
            bfPublishers = true ;
        }
        if(qName.equalsIgnoreCase(COUNTRIES)){
            countries = new ArrayList<Countries>();
            bfCountries = true ;
        }
        if(qName.equalsIgnoreCase(LANGUAGEOFENTITY)){
            languageOfEntities = new ArrayList<LanguageOfEntity>();
            bfLanguageOfEntity = true ;
        }
        if(qName.equalsIgnoreCase(NATIONALITYENTITY)){
            nationalityEnties = new ArrayList<NationalityOfEntity>();
            bfNationalityEntity = true ;
        }
 
        if(qName.equalsIgnoreCase(DATA)){
            dataTitle = new Titre();
            dataCoAuthor = new CoAuthors();
            dataPublishers = new Publishers();
            dataCountry = new Countries() ;
            dataLanguageEntity = new LanguageOfEntity();
            dataNationality = new NationalityOfEntity();
            bfData = true;
        }
        if(qName.equalsIgnoreCase(SOURCES)){
            sourcesData = new ArrayList<String>();
        }
        if(qName.equalsIgnoreCase(NAMETYPE)){
            libraries = new ArrayList<String>(); // magouille pour ne créer quune fois le liste des bibli
        }
        if(qName.equalsIgnoreCase(LIBRARY)){
            bfLibrary = true ;
        }
        if(qName.equalsIgnoreCase(TEXT)){
            bfText = true ;
        }
        if(qName.equalsIgnoreCase(SOURCE)){//ns2:S
            bfSource = true ;
        }
    }
 
    public void endElement(String uri, String localName,
            String qName) throws SAXException {
        if(qName.equalsIgnoreCase(RESPONSE)){
            setViaf(viaf);
            viaf.setLibraries(libraries);
        }
        ///Source -----------------------------
        if(qName.equalsIgnoreCase(SOURCES)){
            if(dataTitle!=null && !sourcesData.isEmpty()){
                dataTitle.setSource(sourcesData);
            }
            if(dataCoAuthor!=null && !sourcesData.isEmpty()){
                dataCoAuthor.setSource(sourcesData);
            }
            if(dataPublishers!=null && !sourcesData.isEmpty()){
                dataPublishers.setSource(sourcesData);
            }
            if(dataCountry!=null && !sourcesData.isEmpty()){
                dataCountry.setSource(sourcesData);
            }
            if(dataLanguageEntity!=null && !sourcesData.isEmpty()){
                dataLanguageEntity.setSource(sourcesData);
            }
            if(dataNationality!=null && !sourcesData.isEmpty()){
                dataNationality.setSource(sourcesData);
            }
        }
 
        ///Data -----------------------------
        if(qName.equalsIgnoreCase(DATA)){
            if(titres!=null && dataTitle.getText()!=null)//on evite les texte null
            titres.add(dataTitle);
            if(coAuthors!=null && dataCoAuthor.getText()!=null)
            coAuthors.add(dataCoAuthor);
            if(publishers!=null && dataPublishers.getText()!=null)
            publishers.add(dataPublishers);
            if(countries!=null && dataCountry.getText()!=null)
            countries.add(dataCountry);
            if(languageOfEntities!=null && dataLanguageEntity.getText()!=null)
                languageOfEntities.add(dataLanguageEntity);
            if(nationalityEnties!=null && dataNationality.getText()!=null)
                nationalityEnties.add(dataNationality);
 
            bfData =false;
        }
        //Case TITLE
        if(qName.equalsIgnoreCase(TITLES)){
            viaf.setTitre(titres);
            bfTitles = false ;
        }
        //Case Coauthors
        if(qName.equalsIgnoreCase(COAUTHORS)){
            viaf.setCoauthors(coAuthors);
            bfcoAuthors = false ;
        }
        //cas Publishers
        if(qName.equalsIgnoreCase(PUBLISHERS)){
            viaf.setPublishers(publishers);
            bfPublishers = false ;
        }
        //cas countries
        if(qName.equalsIgnoreCase(COUNTRIES)){
            viaf.setCountries(countries);
            bfCountries = false ;
        }
        //cas Countries
        if(qName.equalsIgnoreCase(COUNTRIES)){
            viaf.setCountries(countries);
            bfCountries = false ;
        }
        //case Language entity
        if(qName.equalsIgnoreCase(LANGUAGEOFENTITY)){
            viaf.setLanguageOfEntity(languageOfEntities);
            bfLanguageOfEntity = false ;
        }
        //Case Nationality
        if(qName.equalsIgnoreCase(NATIONALITYENTITY)){
            bfNationalityEntity = false ;
            viaf.setNationalityOfEntity(nationalityEnties);
        }
    }
    public void characters(char ch[], int start, int length) throws SAXException {
        String resultParse = new String(ch,start,length);
        if(resultParse!=null && !"".equals(resultParse)){
            if(bfViafId){
                viaf.setViafId(resultParse);
                bfViafId = false ;
            }
            if(bfLibrary){
                libraries.add(resultParse);
                bfLibrary=false ;
            }
            if(bfBirthDate){
                viaf.setBirthDate(resultParse);
                bfBirthDate = false ;
            }
            if(bfDateType){
                viaf.setDateType(resultParse);
                bfDateType = false ;
            }
            if(bfDeathDate){
                if("0".equals(resultParse)){
                    viaf.setDeathDate(" - ");
                }
                else{
                    viaf.setDeathDate(resultParse);
                }
 
                bfDeathDate = false;
            }
 
            //Cas title ======================
            if(bfSource && bfTitles){
                sourcesData.add(resultParse);
                bfSource = false;
            }
            if(bfText && bfTitles && dataTitle!=null){
                dataTitle.setText(resultParse);
                bfText = false ;
            }
 
            //Cas Coauthors
            if(bfSource && bfcoAuthors){
                sourcesData.add(resultParse);
                bfSource = false;
            }
            if(bfText && bfcoAuthors && dataCoAuthor!=null ){
                dataCoAuthor.setText(resultParse);
                bfText = false ;
            }
 
            //cas Publishers
            if(bfSource && bfPublishers){
                sourcesData.add(resultParse);
                bfSource = false;
            }
            if(bfText && bfPublishers && dataPublishers!=null ){
                dataPublishers.setText(resultParse);
                bfText = false ;
            }
            //cas Countries
            if(bfSource && bfCountries){
                sourcesData.add(resultParse);
                bfSource = false;
            }
            if(bfText && bfCountries && dataCountry!=null ){
                dataCountry.setText(resultParse);
                bfText = false ;
            }
            //LANGUAGEOFENTITY
            if(bfSource && bfLanguageOfEntity){
                sourcesData.add(resultParse);
                bfSource = false;
            }
            if(bfText && bfLanguageOfEntity && dataLanguageEntity!=null ){
                dataLanguageEntity.setText(resultParse);
                bfText = false ;
            }
 
            //Nationalities entity
            if(bfSource && bfNationalityEntity){
                sourcesData.add(resultParse);
                bfSource = false;
            }
            if(bfText && bfNationalityEntity && dataNationality!=null ){
                dataNationality.setText(resultParse);
                bfText = false ;
            }           
 
        }
    }
 
    public Viaf getViaf() {
        return viaf;
    }
 
    public void setViaf(Viaf viaf) {
        this.viaf = viaf;
    }
}