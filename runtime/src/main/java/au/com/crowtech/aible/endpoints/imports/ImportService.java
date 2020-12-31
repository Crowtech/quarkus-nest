package au.com.crowtech.aible.endpoints.imports;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImportService {
    private final Log log = LogFactory.getLog(ImportService.class);
    private Map<String, XlsxImport> state;
    private BatchLoadMode mode;

    public XlsxImport createXlsImport(String key) {
        if (SheetState.getUpdateState().contains(key)) {
            XlsxImportOnline xlsxImportOnline = new XlsxImportOnline(GoogleImportService.getInstance().getService());
            state.put(key, xlsxImportOnline);
            SheetState.removeUpdateState(key);
            log.info("The state it is being updated... " + key);
            return xlsxImportOnline;
        }
        if (state.containsKey(key))
            return state.get(key);
        if (mode == BatchLoadMode.ONLINE) {
            log.info("Creating a new Import service for " + key);
            XlsxImportOnline xlsxImportOnline = new XlsxImportOnline(GoogleImportService.getInstance().getService());
            state.put(key, xlsxImportOnline);
            return xlsxImportOnline;
        } else {
            return null;//new XlsxImportOffline(null);
        }
    }

    public ImportService(BatchLoadMode mode, Map<String, XlsxImport> state) {
        this.mode = mode;
        this.state = state;
    }


    public Map<String, Map<String, String>> fetchEntityEntity(String sheetURI, String sheetName) {
        String key = sheetURI + sheetName;
        XlsxImport createXlsImport = createXlsImport(key);
        try {
            return createXlsImport.mappingRawToHeaderAndValuesFmt(sheetURI, sheetName, DataKeyColumn.CODE_TARGET_PARENT_LINK);
        } catch (Exception e1) {
            return new HashMap<>();
        }
    }
}
