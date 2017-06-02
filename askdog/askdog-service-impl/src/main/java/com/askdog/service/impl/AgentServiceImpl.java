package com.askdog.service.impl;

import com.askdog.dao.repository.AgentRepository;
import com.askdog.model.entity.Agent;
import com.askdog.service.AgentService;
import com.askdog.service.UserService;
import com.askdog.service.bo.agent.AgentDetail;
import com.askdog.service.bo.agent.AmendedAgent;
import com.askdog.service.bo.agent.PureAgent;
import com.askdog.service.exception.ForbiddenException;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cell.AgentCell;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.model.entity.builder.AgentBuilder.agentBuilder;
import static com.askdog.model.common.State.DELETED;
import static com.askdog.service.exception.NotFoundException.Error.AGENT;

@RestController
public class AgentServiceImpl implements AgentService {
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AgentCell agentCell;

    @Nonnull
    @Override
    public com.askdog.service.bo.AgentDetail findDetail(@PathVariable("agentId") long agentId,
                                                        @RequestParam(name = "enableDeleted", defaultValue = "false") boolean enableDeleted) {
        com.askdog.service.bo.AgentDetail agentDetail = agentCell.findDetail(agentId);
        checkState(enableDeleted || !agentDetail.isDeleted(), () -> new NotFoundException(AGENT));
        agentDetail.setOwner(userService.findDetail(agentDetail.getOwner().getId()));
        return agentDetail;
    }

    @Nonnull
    @Override
    public com.askdog.service.bo.AgentDetail findDetailWithState(@RequestParam("userId") long userId,
                                                                 @PathVariable("agentId") long agentId,
                                                                 @RequestParam(name = "enableDeleted", defaultValue = "false") boolean enableDeleted) {
        com.askdog.service.bo.AgentDetail agentDetail = findDetail(agentId, enableDeleted);
        agentCell.fillInState(userId, agentDetail);
        return agentDetail;
    }

    @Nonnull
    @Override
    public AgentDetail findPageDetail(@PathVariable("agentId") long agentId) {
        return new AgentDetail().from(findDetail(agentId, false));
    }

    @Nonnull
    @Override
    public AgentDetail findPageDetailWithState(@RequestParam("userId") long userId,
                                               @PathVariable("agentId") long agentId) {
        return new AgentDetail().from(findDetailWithState(userId, agentId, false));
    }

    @Nonnull
    @Override
    public AgentDetail create(@PathVariable("userId") long userId,
                              @Nonnull @Valid @RequestBody PureAgent pureAgent) {
        Agent agent = agentBuilder()
                .owner(userId)
                .address(pureAgent.getAddress())
                .build();
        Agent savedAgent = agentRepository.save(agent);
        return findPageDetail(savedAgent.getId());
    }

    @Nonnull
    @Override
    public AgentDetail update(@PathVariable("userId") long userId,
                              @PathVariable("agentId") long agentId,
                              @Nonnull @Valid @RequestBody AmendedAgent amendedAgent) {
        com.askdog.service.bo.AgentDetail agentDetail = findDetailWithState(userId, agentId, false);
        checkState(agentDetail.isEditable(), () -> new ForbiddenException(ForbiddenException.Error.UPDATE_AGENT));
        Agent agent = agentCell.findExist(agentId);

        if (!Strings.isNullOrEmpty(amendedAgent.getAddress())) {
            agent.setAddress(amendedAgent.getAddress());
        }

        agentRepository.save(agent);
        agentCell.refreshBasicCache(agentId);

        return findPageDetail(agentId);
    }

    @Override
    public void delete(@PathVariable("userId") long userId, @PathVariable("agentId") long agentId) {
        com.askdog.service.bo.AgentDetail agentDetail = findDetailWithState(userId, agentId, false);
        checkState(agentDetail.isDeletable(), () -> new ForbiddenException(ForbiddenException.Error.DELETE_AGENT));
        Agent agent = agentCell.findExist(agentId);
        agent.setState(DELETED);
        agentRepository.save(agent);
        agentCell.refreshBasicCache(agentId);
    }
}
