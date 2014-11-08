package BugTestPack.ApacheJuddiBug;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;

import org.apache.juddi.keygen.KeyGenerator;
import org.apache.juddi.query.util.DynamicQuery;

//*************************************************************************************
//*********************************************************************************** *
//author Aritra Dhar																* *
//MT12004																			* *
//M.TECH CSE																		* * 
//INFORMATION SECURITY																* *
//IIIT-Delhi																		* *	 
//---------------------------------------------------------------------------------	* *																				* *
/////////////////////////////////////////////////									* *
//The program will do the following::::         //									* *
/////////////////////////////////////////////////									* *
//version 1.0																		* *
//*********************************************************************************** * 
//*************************************************************************************
public class JuddiBug 
{
	private List<String> keyGeneratorKeys = null;
	protected String authorizedName;
	
	public void populateKeyGeneratorKeys(EntityManager em) {
		DynamicQuery getKeysQuery = new DynamicQuery();
		getKeysQuery.append("select t.entityKey from Tmodel t").pad().WHERE().pad();

		DynamicQuery.Parameter pubParam = new DynamicQuery.Parameter("t.authorizedName", 
				 getAuthorizedName(), 
				 DynamicQuery.PREDICATE_EQUALS);

		DynamicQuery.Parameter keyParam = new DynamicQuery.Parameter("UPPER(t.entityKey)", 
				 (DynamicQuery.WILDCARD + KeyGenerator.KEYGENERATOR_SUFFIX).toUpperCase(), 
				 DynamicQuery.PREDICATE_LIKE);
		
		
		getKeysQuery.appendGroupedAnd(pubParam, keyParam);
		Query qry = getKeysQuery.buildJPAQuery(em);
		
		keyGeneratorKeys = qry.getResultList();
	}
	
	@Id
	@Column(name = "authorized_name", nullable = false, length = 255)
	public String getAuthorizedName() {
		return this.authorizedName;
	}
	
	
	public boolean isValidPublisherKey(EntityManager em, String key) {
		if (key == null)
			return false;
		/*
		if (keyGeneratorKeys == null)
			populateKeyGeneratorKeys(em);
		*/

		String keyPartition = key.substring(0, key.lastIndexOf(KeyGenerator.PARTITION_SEPARATOR));
		
		keyGeneratorKeys = Arrays.asList(new String[]{"12123:sad","aaddwr:afsf"});
		
		for (String keyGenKey : keyGeneratorKeys) {
			String keyGenPartition = keyGenKey.substring(0, key.lastIndexOf(KeyGenerator.PARTITION_SEPARATOR));
			if (keyGenPartition.equalsIgnoreCase(keyPartition))
				return true;
		}
		return false;
	}
	
	public static void main(String[] args) 
	{
		EntityManager manager = null;
		new JuddiBug().isValidPublisherKey(manager, "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
	}
}
