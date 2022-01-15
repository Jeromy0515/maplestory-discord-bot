package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import javax.xml.transform.dom.DOMSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

public class MapleStoryCharacter {
    private String avatarImgURL;
    private String worldName;
    private String characterName;
    private int level;
    private long exp;
    private String job;
    private int totalRank;
    private int worldRank;
    private int accountID;

    public MapleStoryCharacter(int accountID) {
        setAccountID(accountID);
        try{
            setCharacterInfoByAccountID();
        } catch (SOAPException e){
            e.printStackTrace();
        }
    }

    /*
    *
    *
    *
    *
    *
    *
    *
    * */
    private void setCharacterInfoByAccountID()
            throws SOAPException {
        try {
            String inputSource = getInputSource(this.accountID);
            setCharacterInfo(inputSource);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    *
    *
    *
    *
    *
    *
    *
    * */
    private void setCharacterInfo(String inputSource)
            throws ParserConfigurationException, SAXException, IOException,SOAPException {
        Document document = parseInfoInXML(getSOAPResult(inputSource));

        NodeList nodeList = document.getElementsByTagName("UserInfo");

        org.w3c.dom.Node node = nodeList.item(0);
        if(node.getNodeType() == Node.ELEMENT_NODE){
            Element element = (Element) node;
            this.avatarImgURL = element.getElementsByTagName("AvatarImgURL").item(0).getTextContent();
            this.worldName = element.getElementsByTagName("WorldName").item(0).getTextContent();
            this.characterName = element.getElementsByTagName("CharacterName").item(0).getTextContent();
            this.level = Integer.parseInt(element.getElementsByTagName("Lev").item(0).getTextContent());
            this.exp = Long.parseLong(element.getElementsByTagName("Exp").item(0).getTextContent());
            this.job = element.getElementsByTagName("JobDetail").item(0).getTextContent();
            this.totalRank = Integer.parseInt(element.getElementsByTagName("TotRank").item(0).getTextContent());
            this.worldRank = Integer.parseInt(element.getElementsByTagName("WorldRank").item(0).getTextContent());
        }
    }

    /*
    *
    *
    *
    *
    *
    *
    * */
    private Document parseInfoInXML(String xml)
            throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));

        /* document.getDocumentElement().normalize() 문서 구조 안정화(?) */
        document.getDocumentElement().normalize();

        return document;
    }

    /*
    *
    *
    *
    *
    * */
    private String getSOAPResult(String inputSource)
            throws SOAPException, ParserConfigurationException, SAXException, IOException{

        String soapResult = null;

        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            DOMSource requestSource = new DOMSource(documentBuilder.parse(new InputSource(new StringReader(inputSource))));

            SOAPMessage requestSOAPMessage = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            SOAPPart requestSOAPPart = requestSOAPMessage.getSOAPPart();
            requestSOAPPart.setContent(requestSource);

            SOAPConnection connection = SOAPConnectionFactory.newInstance().createConnection();

            SOAPMessage responseSOAPMessage = connection.call(requestSOAPMessage, "http://api.maplestory.nexon.com/soap/maplestory.asmx");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            responseSOAPMessage.writeTo(out);
            soapResult = new String(out.toByteArray(), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return soapResult;

    }

    /*
    *
    * getInputSource(int accountID)
    * MapleStory GetCharacterInfoByAccountID SOAP 1.2 API
    * AccoutID에 32bit 정수형(int)값을 넣어주면 해당하는 XML 문자열을 반환합니다..
    *
    * */
    private String getInputSource(int accountID){
        return  "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <GetCharacterInfoByAccountID xmlns=\"https://api.maplestory.nexon.com/soap/\">\n" +
                "      <AccountID>" + accountID + "</AccountID>\n" +
                "    </GetCharacterInfoByAccountID>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
    }

    private void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getAvatarImgURL() {
        return avatarImgURL;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getCharacterName(){
        return this.characterName;
    }

    public int getLevel() {
        return level;
    }

    public long getExp() {
        return exp;
    }

    public String getJob() {
        return job;
    }

    public int getTotalRank() {
        return totalRank;
    }

    public int getWorldRank() {
        return worldRank;
    }
}
