package sg.edu.nus.iss.paf_workshop22_mvc.services;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.RequestEntity.HeadersBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import sg.edu.nus.iss.paf_workshop22_mvc.exceptions.RecordNotFoundException;
import sg.edu.nus.iss.paf_workshop22_mvc.models.Rsvp;

@Service
public class RsvpService {
    
    public List<Rsvp> getAllRsvp() throws ParseException{

        // generate the url
        String url = "http://localhost:8080/api/rsvps";

        // contruct request entity
        RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();

        // construct REST template
        RestTemplate template = new RestTemplate();

        // =========== Auto deserialize Json response into Java Object using ParameterizedTypeReference ===========
        // Make the request & receive response entity, deserialize Json response to List<Rsvp>
        ResponseEntity<List<Rsvp>> resp = template.exchange(req, new ParameterizedTypeReference<List<Rsvp>>() {});

        // try parsing Json to model directly
        List<Rsvp> rsvpList = resp.getBody();

        // =======================

        // // =========== Manually read the Json response into Java Object ===========
        // // Make the request & receive response entity, the payload of the response will be a string
        // ResponseEntity<String> resp = template.exchange(req, String.class);

        // // read payload into Json Object
        // String body = resp.getBody();

        // JsonReader reader = Json.createReader(new StringReader(body));
        // JsonArray arr = reader.readArray();

        // List<Rsvp> rsvpList = new ArrayList<>();
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        // for(int i=0; i<arr.size(); i++){
        //     JsonObject json = arr.getJsonObject(i);
        //     Rsvp rsvp = new Rsvp();
        //     rsvp.setId(json.getInt("id"));
        //     rsvp.setFullname(json.getString("fullname")); // !! class cast exception if value is null but no error if value is empty ""
        //     rsvp.setEmail(json.getString("email"));
        //     rsvp.setPhone(json.getString("phone"));
        //     rsvp.setConfirmationDate(formatter.parse(json.getString("confirmationDate")));
        //     rsvp.setComments(json.getString("comments"));
        //     rsvpList.add(rsvp);

        // }
        // ====================

        return rsvpList;

    }

    // GET api/rsvp?q=name
    // return 404 with error object if rsvp not found
    public List<Rsvp> findRsvpByName(String name) throws ParseException{

        // generate the url
        String URL = "http://localhost:8080/api/rsvp";
        String url = UriComponentsBuilder.fromUriString(URL).queryParam("q", name).toUriString();

        // contruct request entity
        RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();

        // construct REST template
        RestTemplate template = new RestTemplate();

        
        List<Rsvp> rsvpList = new ArrayList<>();


        // =========== Auto deserialize Json response into Java Object using ParameterizedTypeReference ===========
        // Make the request & receive response entity, deserialize Json response to List<Rsvp>
        ResponseEntity<List<Rsvp>> resp = template.exchange(req, new ParameterizedTypeReference<List<Rsvp>>() {});

        // try parsing Json to model directly
        Integer statuscode = resp.getStatusCode().value();
        

        if(statuscode == 200){
            rsvpList = resp.getBody();
            System.out.println(rsvpList);
            return rsvpList;
        }

        return rsvpList; // return empty List

    }

    // GET api/rsvp/{email}
    // return 404 with error object if rsvp not found
    public Rsvp findByEmail(String email){

         // generate the url with path variable
         String URL = "http://localhost:8080/api/rsvp/";
         String url = UriComponentsBuilder.fromUriString(URL).path(email).toUriString();

         // contruct request entity
         RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
 
         // construct REST template
         RestTemplate template = new RestTemplate();
 
         // Make the request & receive response entity, deserialize Json response to Rsvp
         ResponseEntity<Rsvp> resp = template.exchange(req, Rsvp.class);
 
         // try parsing Json to model directly
         Rsvp rsvp = resp.getBody();

         return rsvp;
    }

    // POST /api/rsvp
    // Content-Type: application/x-www-form-urlencoded
    // return 201 if successful
    public Boolean addRsvp(Rsvp rsvp){

        // convert rsvp object to a multivaluemap
        MultiValueMap<String,String> map = rsvp.toMultiValueMap();

        // generate the url
        String url = "http://localhost:8080/api/rsvp";

        // contruct request entity, .accept() first followed by .body()
        RequestEntity<MultiValueMap<String,String>> req = RequestEntity.post(url)
                                                                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                                    .accept(MediaType.APPLICATION_JSON)
                                                                    .body(map,MultiValueMap.class);

        // construct REST template
        RestTemplate template = new RestTemplate();

        try{
            // Make the request & receive response entity status code & payload
            ResponseEntity<String> resp = template.exchange(req,String.class);
            Integer statuscode = resp.getStatusCode().value();
            String payload = resp.getBody();

            // read the payload
            JsonReader reader = Json.createReader(new StringReader(payload));

            if(statuscode == 201){
                JsonValue jsonValue = reader.readValue();
                Boolean updated = Boolean.parseBoolean(jsonValue.toString());
                return updated;
            }
            if(statuscode == 501){
                JsonObject jsonObject = reader.readObject();
                String message = jsonObject.getString("message");
                System.out.println(message); // use logger
                return false;
            }

            return false;

        }catch(Exception ex){
            return false;
        }

    }
    

    // PUT /api/rsvp/fred@gmail.com
    // Content-Type: application/x-www-form-urlencoded
    // REST endpoint return 201 if successful, 404 if email not found
    public Boolean updateRsvp(Rsvp rsvp){

        // convert rsvp object to a multivaluemap
        MultiValueMap<String,String> map = rsvp.toMultiValueMap();

        // generate the url
        String URL = "http://localhost:8080/api/rsvp/";
        String url = UriComponentsBuilder.fromUriString(URL).path(rsvp.getEmail()).toUriString();

        // contruct request entity, .accept() first followed by .body()
        RequestEntity<MultiValueMap<String,String>> req = RequestEntity.put(url)
                                                                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                                    .accept(MediaType.APPLICATION_JSON)
                                                                    .body(map,MultiValueMap.class);

        // construct REST template
        RestTemplate template = new RestTemplate();

        try{
            // Make the request & receive response entity status code & payload
            ResponseEntity<String> resp = template.exchange(req,String.class);
            Integer statuscode = resp.getStatusCode().value();
            String payload = resp.getBody();

            // read the payload
            JsonReader reader = Json.createReader(new StringReader(payload));

            if(statuscode == 201){
                JsonValue jsonValue = reader.readValue();
                Boolean updated = Boolean.parseBoolean(jsonValue.toString());
                return updated;
            }
            if(statuscode == 404){
                JsonObject jsonObject = reader.readObject();
                String message = jsonObject.getString("message");
                return false;
            }

            return false;

        }catch(Exception ex){
            return false;
        }

    }
    

}
