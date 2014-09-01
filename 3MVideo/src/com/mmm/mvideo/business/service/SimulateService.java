package com.mmm.mvideo.business.service;

import java.util.ArrayList;

import com.mmm.mvideo.business.entity.MMMVideoItem;

// TODO: Auto-generated Javadoc
/**
 * The Class SimulateService.
 * @author a37wczz
 */
public class SimulateService {
    
    /** The instance. */
    private static SimulateService instance = new SimulateService();

    /**
     * Instantiates a new simulate service.
     */
    private SimulateService() {
        super();
    }

    /**
     * Gets the single instance of SimulateService.
     *
     * @return single instance of SimulateService
     */
    public static SimulateService getInstance() {
        return instance;
    }

    
    public ArrayList<MMMVideoItem> getAllVideoGroups(){
    	ArrayList<MMMVideoItem> list = new ArrayList<MMMVideoItem>();
    	MMMVideoItem tabGroup = new MMMVideoItem();
    	list.add(tabGroup);
    	tabGroup.setTitle("AAD");
    	tabGroup.setImgName("aad_montage_v1_2.png");
    	//group 1
    	MMMVideoItem group = new MMMVideoItem();
    	tabGroup.addVideoItem(group);
    	group.setTitle("Industrial Spray Solutions");
    	MMMVideoItem videoItem = new MMMVideoItem();
    	videoItem.setTitle("Spray Equipment");
    	videoItem.setTags("aad,industrial,spray,solutions,accuspray");
    	videoItem.setToken("P_ar-cvZG1jTU6tQ0goxcfjrlr0BVDn-Nrx1b9XPNuKXlXPmWzmw0A..");
    	group.addVideoItem(videoItem);
    	
    	videoItem = new MMMVideoItem();
    	videoItem.setTitle("PPS");
    	videoItem.setTags("aad,industrial,spray,solutions,pps");
    	videoItem.setToken("P_ar-cvZG1jTU6tQ0goxcfjrlr0BVDn-Nrx1b9XPNuKXlXPmWzmw0A..");
    	group.addVideoItem(videoItem);
    	
    	videoItem = new MMMVideoItem();
    	videoItem.setTitle("Dirt Trap");
    	videoItem.setTags("aad,industrial,spray,solutions,dirt,trap");
    	videoItem.setToken("P_ar-cvZG1jTU6tQ0goxcfjrlr0BVDn-Nrx1b9XPNuKXlXPmWzmw0A..");
    	group.addVideoItem(videoItem);
    	
    	videoItem = new MMMVideoItem();
    	videoItem.setTitle("DMS");
    	videoItem.setTags("aad,industrial,dms");
    	videoItem.setToken("P_ar-cvZG1jTU6tQ0goxcfjrlr0BVDn-Nrx1b9XPNuKXlXPmWzmw0A..");
    	group.addVideoItem(videoItem);
    	
    	//group 2
    	group = new MMMVideoItem();
    	tabGroup.addVideoItem(group);
    	group.setTitle("Collision");
    	videoItem = new MMMVideoItem();
    	group.addVideoItem(videoItem);
    	videoItem.setTitle("Masking");
    	//subItem1
    	MMMVideoItem subItem = new MMMVideoItem();
    	subItem.setTitle("Dirt Trap Booth Protection");
    	subItem.setTags("aad,Collision,Masking,Dirt,trap,booth,protection");
    	subItem.setToken("P_ar-cvZG1jTU6tQ0goxcfjrlr0BVDn-Nrx1b9XPNuKXlXPmWzmw0A..");
    	videoItem.addVideoItem(subItem);
    	
    	//subItem2
    	subItem = new MMMVideoItem();
    	subItem.setTitle("233 Plus Masking Tape");
    	subItem.setTags("aad,Collision,Masking,233,plus,tape");
    	subItem.setToken("P_ar-cvZG1jTU6tQ0goxcfjrlr0BVDn-Nrx1b9XPNuKXlXPmWzmw0A..");
    	videoItem.addVideoItem(subItem);
    	
    	
    	//tab2
    	tabGroup = new MMMVideoItem();
    	list.add(tabGroup);
    	tabGroup.setTitle("IATD");
    	tabGroup.setImgName("iatdmontage.png");
    	//group 1
    	group = new MMMVideoItem();
    	tabGroup.addVideoItem(group);
    	group.setTitle("Packaging");
    	videoItem = new MMMVideoItem();
    	videoItem.setTitle("Box Sealing Tapes");
    	videoItem.setTags("iatd,packaging,box,sealing,tapes");
    	videoItem.setToken("P_ar-cvZG1jTU6tQ0goxcfjrlr0BVDn-Nrx1b9XPNuKXlXPmWzmw0A..");
    	group.addVideoItem(videoItem);
    	
    	videoItem = new MMMVideoItem();
    	videoItem.setTitle("PDFs");
    	group.addVideoItem(videoItem);
    	//pdf
     	//subItem1
    	subItem = new MMMVideoItem();
    	subItem.setTitle("Scotch® Premium Box Sealing Tapes");
    	subItem.setUrl("http://multimedia.3m.com/mws/mediawebserver?66666UF6EVsSyXTtMxf2mXTcEVtQEVs6EVs6EVs6E666666--");
    	subItem.setDesc("");
    	videoItem.addVideoItem(subItem);
    	
     	//subItem2
    	subItem = new MMMVideoItem();
    	subItem.setTitle("Manual Box Sealing Tape Dispensers");
    	subItem.setUrl("http://multimedia.3m.com/mws/mediawebserver?SSSSSufSevTsZxtUoxt15x_SevUqevTSevTSevTSeSSSSSS--");
    	subItem.setDesc("");
    	videoItem.addVideoItem(subItem);
    	
    	videoItem = new MMMVideoItem();
    	videoItem.setTitle("Equipment");
    	videoItem.setTags("iatd,packaging,equipment");
    	videoItem.setToken("P_ar-cvZG1jTU6tQ0goxcfjrlr0BVDn-Nrx1b9XPNuKXlXPmWzmw0A..");
    	group.addVideoItem(videoItem);
    	
    	return list;
    	
    	
    }

}
