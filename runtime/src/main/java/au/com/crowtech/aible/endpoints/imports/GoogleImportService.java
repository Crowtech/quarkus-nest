package au.com.crowtech.aible.endpoints.imports;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

public class GoogleImportService {
	private static final Logger log = Logger.getLogger(GoogleImportService.class);
	 

	@ConfigProperty(name = "import.sheetid.googlepath", defaultValue = "token-secret-service-account.json")
	static String staticGooglePath;

	

   private final JsonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

    private HttpTransport httpTransport;

    private final List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS);

    private Sheets service;

    public Sheets getService() {
        return service;
    }

    private static GoogleImportService instance = null;

    public static GoogleImportService getInstance(String googlePath) {
        synchronized (GoogleImportService.class) {
            if (instance == null) {
                instance = new GoogleImportService(googlePath);
            }
        }
        return instance;
    }
    
    public static GoogleImportService getInstance() {
        synchronized (GoogleImportService.class) {
            if (instance == null) {
                log.error("No Security setup instance");
                instance = new GoogleImportService(staticGooglePath);
            }
        }
        return instance;
    }


    private GoogleImportService(String googlePath) {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            service = getSheetsService(googlePath);
        } catch (final IOException e) {
            log.error(e.getMessage());
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            System.exit(1);
        }
    }


    public Sheets getSheetsService(String googlePath) throws IOException {
        final Credential credential = authorize(googlePath);
        return new Sheets.Builder(httpTransport, jacksonFactory,
                credential).build();
    }

    public Credential authorize(String googlePath) throws IOException {
        Optional<String> path = Optional.ofNullable(googlePath);
        if (!path.isPresent()) {
            throw new FileNotFoundException("googlePath not set");
        }

        log.info("GooglePath = "+path.get());
        
//        java.nio.file.Path path2 = null;
//        try {
//			path2 = Paths.get(getClass().getClassLoader()
//				      .getResource(googlePath).toURI());
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        log.info("got to here");
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(googlePath),
                httpTransport, jacksonFactory).createScoped(scopes);
        log.info("got to here2");
        if (credential != null) {
            String msg = String.format("Spreadsheets being read with user id: %s.", credential.getServiceAccountId());
            log.info("Credential text "+msg);
            log.info(credential.getTokenServerEncodedUrl());
        }
        return credential;
    }

    public  String buffer(String filepath) throws IOException {
    	 String contents  = null;
        try (InputStream inputStream = getClass().getResourceAsStream(filepath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            contents = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        }
        return contents;
    }
    
    InputStream getResourceFile(String path) {
    	return this.getClass().getResourceAsStream(path);  
    }
}
