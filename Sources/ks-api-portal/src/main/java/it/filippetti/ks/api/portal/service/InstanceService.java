/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.filippetti.ks.api.portal.dto.BpmInstanceDTO;
import it.filippetti.ks.api.portal.dto.CreateInstanceDTO;
import it.filippetti.ks.api.portal.dto.InstanceDTO;
import it.filippetti.ks.api.portal.dto.PageDTO;
import it.filippetti.ks.api.portal.dto.UpdateInstanceDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.exception.AuthorizationException;
import it.filippetti.ks.api.portal.exception.BusinessError;
import it.filippetti.ks.api.portal.exception.BusinessException;
import it.filippetti.ks.api.portal.exception.ExpiredException;
import it.filippetti.ks.api.portal.exception.NotFoundException;
import it.filippetti.ks.api.portal.mapper.MappingContext;
import it.filippetti.ks.api.portal.mapper.dto.InstanceMapper;
import it.filippetti.ks.api.portal.model.AuthenticationContext;
import it.filippetti.ks.api.portal.model.Instance;
import it.filippetti.ks.api.portal.model.Pager;
import it.filippetti.ks.api.portal.model.Proceeding;
import it.filippetti.ks.api.portal.repository.InstanceRepository;
import it.filippetti.ks.api.portal.repository.ProceedingRepository;
import it.filippetti.ks.api.portal.utilities.MicroserviceUtilities;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author dino
 */
@Service
public class InstanceService {
    
    private static final Logger log = LoggerFactory.getLogger(InstanceService.class);
    
    @Autowired
    private InstanceRepository instanceRepository;
    
    @Autowired
    private ProceedingRepository proceedingRepository;
    
    @Autowired
    private InstanceMapper instanceMapper;
    
    @Value( "${application.bpmBaseUrl}" )
    private String BASE_URL;

    @Autowired
    private ObjectMapper objectMapper;

    
    /**
     * 
     * @param context
     * @param createInstanceDTO
     * @return
     * @throws ApplicationException
     * @throws IOException 
     */
    public InstanceDTO create(AuthenticationContext context, CreateInstanceDTO createInstanceDTO) throws ApplicationException, IOException{
        
        if (!(context.isAdmin() || context.isUser())) {
            throw new AuthorizationException();
        }
        
        log.info("Request to create Instance: {}", createInstanceDTO.toString());
        Date now = new Date();
        
        Proceeding proceeding = proceedingRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), createInstanceDTO.getProceedingId());
        Instance instance = new Instance(
            proceeding,
            now,
            createInstanceDTO.getModel(),
            null,
            Boolean.FALSE,
            null,
            context.getTenant(),
            context.getOrganization(),
            context.getUserId()
        );
        
        return instanceMapper.map(instanceRepository.save(instance), MappingContext.of(context));
    }
    
    
    /**
     * 
     * @param context
     * @param updateInstanceDTO
     * @return
     * @throws ApplicationException
     * @throws IOException 
     */
    public InstanceDTO update(AuthenticationContext context, UpdateInstanceDTO updateInstanceDTO) 
        throws ApplicationException, IOException {
        
        Instance instance;
        BpmInstanceDTO bpmInstance;

        if (!(context.isAdmin() || context.isUser())) {
            throw new AuthorizationException();
        }

        log.info("Request to update Instance: {}", updateInstanceDTO.toString());
        
        instance = instanceRepository.findByTenantAndOrganizationAndId(
            context.getTenant(), context.getOrganization(), updateInstanceDTO.getId());
        if (instance == null) {
            throw new NotFoundException();
        }
        if (instance.getBpmInstanceId() == null) {
            if (updateInstanceDTO.getSend()) {
                
                if(!checkProceedingDates(instance.getProceeding())) {
                    throw new ExpiredException();
                }
                
                instance.setSent(Boolean.TRUE);
                instance.setDispatchDate(new Date());
                instance.setModel(updateInstanceDTO.getModel());
                instance = instanceRepository.save(instance);

                bpmInstance = createBPMInstance(
                    instance.getId().toString(),
                    instance.getProceeding().getProcessId(),
                    updateInstanceDTO.getInput(), 
                    context.getJwtToken());

                instance.setBpmInstanceId(bpmInstance.getId());
                instance = instanceRepository.save(instance);
            }
            else {
                instance.setModel(updateInstanceDTO.getModel());
                instance = instanceRepository.save(instance);
            }
        }
        return instanceMapper.map(instance, MappingContext.of(context));
    }
    
    
    private boolean checkProceedingDates(Proceeding p) {
        if(p.getSendIfExpired())
            return true;
        
        Date actualDate = new Date();
        return p.getStartDate().before(actualDate) && p.getEndDate().after(actualDate);
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @throws ApplicationException 
     */
    public void delete(AuthenticationContext context, Long instanceId)throws ApplicationException{

        if (!(context.isAdmin() || context.isUser())) {
            throw new AuthorizationException();
        }
        
        log.info("Request to delete Instance with id " + instanceId);
        instanceRepository.deleteById(instanceId);
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @return
     * @throws ApplicationException 
     */
    public InstanceDTO findOne(AuthenticationContext context, Long instanceId) throws ApplicationException{
        
        log.info("Request to get one Instance with id " + instanceId);
        Instance instance = instanceRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), instanceId);
        if(instance == null){
            throw new NotFoundException();
        }
        return instanceMapper.map(instance, MappingContext.of(context));
    }
    
    /**
     * 
     * @param context
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param sent
     * @return
     * @throws ApplicationException 
     */
    public PageDTO<InstanceDTO> findAllPaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy, Boolean sent)
        throws ApplicationException{
        
        log.info("Request to get paged Instances");
        return instanceMapper.map(instanceRepository.findAll(context.getTenant(), context.getOrganization(), Pager.of(Instance.class, pageNumber, pageSize, orderBy), context.getUserId(), sent), MappingContext.of(context));
    }
    
    /**
     * 
     * @param context
     * @param instanceId
     * @return
     * @throws ApplicationException 
     */
    public InstanceDTO findOneByBpmInstanceId(AuthenticationContext context, Long instanceId) throws ApplicationException{
        
        log.info("Request to get one Instance by BPM instance id (BPMInstanceId='" + instanceId + "')");
        Instance instance = instanceRepository.findByTenantAndOrganizationAndBpmInstanceId(context.getTenant(), context.getOrganization(), instanceId);
        if(instance == null){
            throw new NotFoundException();
        }
        return instanceMapper.map(instance, MappingContext.of(context));
    }
    
    /**
     * 
     * @param context
     * @param sent
     * @return
     * @throws ApplicationException 
     */
    public List<InstanceDTO> findAll(AuthenticationContext context, Boolean sent) throws ApplicationException{
        
        log.info("Request to get all instance by user");
        return instanceMapper.map(
                instanceRepository.findAllByTenantAndOrganizationAndUserIdAndSent(context.getTenant(), context.getOrganization(), context.getUserId(), sent),
                MappingContext.of(context)
        );
    }
    
    /**
     * 
     * @param context
     * @param proceedingId
     * @return 
     */
    public Boolean checkForUniqueInstance(AuthenticationContext context, Long proceedingId) throws ApplicationException {
        
        log.info("Request to check if the user can compile unique instance");
        Long count = instanceRepository.countByTenantAndOrganizationAndUserIdAndProceedingId(context.getTenant(), context.getOrganization(), context.getUserId(), proceedingId);
        if(count > 0){
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 
     * @param context
     * @param proceedingId
     * @return 
     */
    public InstanceDTO getForUniqueInstance(AuthenticationContext context, Long proceedingId){
        log.info("Request to get instance if the user can compile unique instance");
        List<Instance> i = instanceRepository.findByTenantAndOrganizationAndUserIdAndProceedingId(
                context.getTenant(), context.getOrganization(), context.getUserId(), proceedingId);
        if(i != null && i.size() > 0){
            return instanceMapper.map(i.get(0), MappingContext.of(context));
        } else {
            return null;
        }
    }
    
    /**
     * 
     * @param context
     * @param proceedingId
     * @return
     * @throws ApplicationException 
     */
    public Boolean checkForSingleInstance(AuthenticationContext context, Long proceedingId) throws ApplicationException{

        log.info("Request to check if the user can compile single instance");
        
        Long count = instanceRepository.countDrafts(context.getTenant(), context.getOrganization(), context.getUserId(), proceedingId);
        if(count > 0){
            // Ho una istanza in bozza
            return false;
        } else {
            List<Instance> instancesSent = instanceRepository
                    .findAllByTenantAndOrganizationAndUserIdAndSentAndProceedingIdAndBpmInstanceIdIsNotNull(context.getTenant(), context.getOrganization(), context.getUserId(), true, proceedingId);
            for(Instance instance : instancesSent){
                String url = "instances/" + instance.getBpmInstanceId();
                Object bpmInstance = MicroserviceUtilities.callToMicroservice(context, BASE_URL, url, HttpMethod.GET);
                Map<String, Object> map = new HashMap<>();
                if(bpmInstance instanceof Map){
                    map.putAll((Map)bpmInstance);
                    if(map.get("status") instanceof Integer){
                        int status = (Integer)map.get("status");
                        if(status == 1){
                            return false;
                        }
                    } else {
                        throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR);
                    }
                } else {
                    throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR);
                }
            }
            return true;
        }
        
//        List<Instance> instancesNotSended = instanceRepository.findAllByTenantAndOrganizationAndUserIdAndSentAndProceedingId(context.getTenant(), context.getOrganization(), context.getUserId(), false, proceedingId);
//        List<Instance> instancesSended = instanceRepository.findAllByTenantAndOrganizationAndUserIdAndSentAndProceedingId(context.getTenant(), context.getOrganization(), context.getUserId(), true, proceedingId);
//        if(instancesNotSended.size() > 0){
//            return false;
//        } else {
//            for(Instance instance : instancesSended){
//                String url = "instances/" + instance.getBpmInstanceId();
//                Object bpmInstance = MicroserviceUtilities.callToMicroservice(context, BASE_URL, url, HttpMethod.GET);
//                Map<String, Object> map = new HashMap<>();
//                if(bpmInstance instanceof Map){
//                    map.putAll((Map)bpmInstance);
//                    if(map.get("status") instanceof Integer){
//                        int status = (Integer)map.get("status");
//                        if(status != 2){
//                            return false;
//                        }
//                    } else {
//                        throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR);
//                    }
//                } else {
//                    throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR);
//                }
//            }
//            return true;
//        }
    }
    
    
    /**
     * 
     * @param context
     * @param proceedingId
     * @return
     * @throws ApplicationException 
     */
    public InstanceDTO getForSingleInstance(AuthenticationContext context, Long proceedingId) throws ApplicationException{
        log.info("Request to get instance user can compile single instance");
        
        List<Instance> i = instanceRepository.findDrafts(context.getTenant(), context.getOrganization(), context.getUserId(), proceedingId);
        if(i != null && i.size() > 0){
            // Ho una istanza in bozza
            log.info("getForSingleInstance i1 {} ", i.get(0));
            return instanceMapper.map(i.get(0), MappingContext.of(context));
        } else {
            List<Instance> instancesSent = instanceRepository
                    .findAllByTenantAndOrganizationAndUserIdAndSentAndProceedingIdAndBpmInstanceIdIsNotNull(context.getTenant(), context.getOrganization(), context.getUserId(), true, proceedingId);
            for(Instance instance : instancesSent){
                String url = "instances/" + instance.getBpmInstanceId();
                Object bpmInstance = MicroserviceUtilities.callToMicroservice(context, BASE_URL, url, HttpMethod.GET);
                Map<String, Object> map = new HashMap<>();
                if(bpmInstance instanceof Map){
                    map.putAll((Map)bpmInstance);
                    if(map.get("status") instanceof Integer){
                        int status = (Integer)map.get("status");
                        if(status == 1){
                            log.info("getForSingleInstance i2 {} ", instance);
                            return instanceMapper.map(instance, MappingContext.of(context));
                        }
                    } else {
                        throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR);
                    }
                } else {
                    throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR);
                }
            }
            return null;
        }
    }
    
    
    /**
     * @param context
     * @return the total number of instances made by tenant and organization
     * @throws ApplicationException 
     */
    public Integer count(AuthenticationContext context) throws ApplicationException{
        
        log.info("Request to get the number of Instances");
        return instanceRepository
                .countByTenantAndOrganization(context.getTenant(), context.getOrganization());
    }
    
        
    /**
     * @param context
     * @return the total number of documents sent by users by tenant and organization
     * @throws ApplicationException 
     */
    public Integer countReceivedDocuments(AuthenticationContext context) throws ApplicationException{

        log.info("Request to get the number received documents");
        return 0;
    }
    
    
    /**
     * @param context
     * @return the total number of documents sent to users by tenant and organization
     * @throws ApplicationException 
     */
    public Integer countSentDocuments(AuthenticationContext context) throws ApplicationException{
        
        log.info("Request to get the number of sent documents");
        return 0;
    }
    
    
     /**
     * @param context
     * @param startDate
     * @param endDate
     * @return the total total amount of payments made by tenant and organization
     * @throws ApplicationException 
     */
    public List<Object[]> countByType(AuthenticationContext context, Date startDate, Date endDate) throws ApplicationException{
        
        log.info("Request to get the total number of instances by type from {} to {}", startDate, endDate);
        return instanceRepository.getTotalsByMonth(context.getTenant(), context.getOrganization(), startDate, endDate);
    }
    
    
    /**
     * @param context
     * @return the total total amount of payments made by tenant and organization
     * @throws ApplicationException 
     */
    public Map<String, Integer> countByStatus(AuthenticationContext context) throws ApplicationException{
        
        log.info("Request to get the total number of instances by status");
       
        Map<String, Integer> res = new HashMap();
        
        String url1 = "instances/?activeOnly=false&pageNumber=1&pageSize=1&rootOnly=false&searchableOnly=true&status=1";
        res.put("Status1", callBPM(context, url1));
        
        String url2 = "instances/?activeOnly=false&pageNumber=1&pageSize=1&rootOnly=false&searchableOnly=true&status=2";
        res.put("Status2", callBPM(context, url2));
        
        String url3 = "instances/?activeOnly=false&pageNumber=1&pageSize=1&rootOnly=false&searchableOnly=true&status=3";
        res.put("Status3", callBPM(context, url3));
        
        return res;
    }
    
    

    private Integer callBPM(AuthenticationContext context, String url) throws ApplicationException {
        Object bpmInstance = MicroserviceUtilities.callToMicroservice(context, BASE_URL, url, HttpMethod.GET);
        Map<String, Object> map = new HashMap<>();
        
        if(bpmInstance instanceof Map){
            map.putAll((Map)bpmInstance);
            log.info("Request to get the total number of instances by status -> MAP {}", map);
            if(map.get("count") instanceof Integer){
                int count = (Integer)map.get("count");
                log.info("Request to get the total number of instances by status -> COUNT {}", count);
                return count;
            } else {
                return 0;
            }
        } else {
            throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR);
        }
    }
    
    
    /**
     * 
     * @param operationId
     * @param processId
     * @param input
     * @param token
     * @return 
     */
    private BpmInstanceDTO createBPMInstance(String operationId, String processId, Object input, String token) throws IOException {
        Map json = new HashMap();
        json.put("operationId", operationId);
        json.put("processId", processId);
        json.put("input", input);
        
        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8));

        String url = BASE_URL + "/instances";
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(json), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<BpmInstanceDTO> responseEntity = 
            restTemplate.postForEntity(url, requestEntity, BpmInstanceDTO.class);

        log.info("Request to create Instance - BPM Body: {} ", responseEntity.getBody());

        return responseEntity.getBody();
    }
}
