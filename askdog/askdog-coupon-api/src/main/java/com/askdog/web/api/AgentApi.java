package com.askdog.web.api;

import com.askdog.service.AgentService;
import com.askdog.service.bo.agent.AgentDetail;
import com.askdog.service.bo.agent.AmendedAgent;
import com.askdog.service.bo.agent.PureAgent;
import com.askdog.service.exception.ServiceException;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/agent")
public class AgentApi {

    @Autowired
    private AgentService agentService;

    @Nonnull
    @RequestMapping(value = "/{agentId}", method = GET)
    AgentDetail getAgentDetail(@PathVariable("agentId") long agentId) {
        return agentService.findPageDetail(agentId);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = POST)
    public AgentDetail create(@AuthenticationPrincipal AdUserDetails user,
                              @Valid @RequestBody PureAgent pureAgent) throws ServiceException {
        return agentService.create(user.getId(), pureAgent);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{agentId}", method = PUT)
    public AgentDetail update(@AuthenticationPrincipal AdUserDetails user,
                              @PathVariable("agentId") Long agentId,
                              @Valid @RequestBody AmendedAgent amendedAgent) throws ServiceException {
        return agentService.update(user.getId(), agentId, amendedAgent);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{agentId}", method = DELETE)
    public void delete(@AuthenticationPrincipal AdUserDetails user,
                       @PathVariable("agentId") Long agentId) throws ServiceException {
        agentService.delete(user.getId(), agentId);
    }
}
