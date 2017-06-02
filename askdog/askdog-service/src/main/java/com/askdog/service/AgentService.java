package com.askdog.service;

import com.askdog.service.bo.agent.AgentDetail;
import com.askdog.service.bo.agent.AmendedAgent;
import com.askdog.service.bo.agent.PureAgent;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@FeignClient("service")
@RequestMapping("/service/agent")
public interface AgentService {

    @Nonnull
    @RequestMapping("/_internal/{agentId}")
    com.askdog.service.bo.AgentDetail findDetail(@PathVariable("agentId") long agentId,
                                                 @RequestParam(name = "enableDeleted", defaultValue = "false") boolean enableDeleted);

    @Nonnull
    @RequestMapping("/_internal/{agentId}/state")
    com.askdog.service.bo.AgentDetail findDetailWithState(@RequestParam("userId") long userId,
                                                          @PathVariable("agentId") long agentId,
                                                          @RequestParam(name = "enableDeleted", defaultValue = "false") boolean enableDeleted);

    @Nonnull
    @RequestMapping("/{agentId}")
    AgentDetail findPageDetail(@PathVariable("agentId") long agentId);

    @Nonnull
    @RequestMapping("/{agentId}/state")
    AgentDetail findPageDetailWithState(@RequestParam("userId") long userId,
                                        @PathVariable("agentId") long agentId);

    @Nonnull
    @RequestMapping(value = "/user/{userId}", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    AgentDetail create(@PathVariable("userId") long userId,
                       @Nonnull @Valid @RequestBody PureAgent pureAgent);

    @Nonnull
    @RequestMapping(value = "/user/{userId}/{agentId}", method = PUT, produces = APPLICATION_JSON_UTF8_VALUE)
    AgentDetail update(@PathVariable("userId") long userId,
                       @PathVariable("agentId") long agentId,
                       @Nonnull @Valid @RequestBody AmendedAgent amendedAgent);

    @RequestMapping(value = "/user/{userId}/{agentId}", method = DELETE)
    void delete(@PathVariable("userId") long userId,
                @PathVariable("agentId") long agentId);
}
