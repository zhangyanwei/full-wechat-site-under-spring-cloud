package com.askdog.service.impl.cell;

import com.askdog.dao.repository.AgentRepository;
import com.askdog.model.entity.Agent;
import com.askdog.model.common.State;
import com.askdog.service.bo.AgentDetail;
import com.askdog.service.bo.UserDetail;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cache.annotation.agent.AgentBasicCache;
import com.askdog.service.impl.cache.annotation.agent.AgentBasicCacheRefresh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

import static com.askdog.model.common.State.DELETED;
import static com.askdog.service.exception.NotFoundException.Error.AGENT;
import static com.google.common.collect.Sets.newHashSet;

@Component
@Transactional
public class AgentCell {

    @Autowired
    private AgentRepository agentRepository;

    @Nonnull
    @AgentBasicCache
    public AgentDetail findDetail(long agentId) {
        Agent agent = agentRepository.findById(agentId).orElseThrow(() -> new NotFoundException(AGENT));
        AgentDetail agentDetail = new AgentDetail();
        agentDetail.setId(agent.getId());
        agentDetail.setAddress(agent.getAddress());
        agentDetail.setCreationTime(agent.getCreationTime());
        agentDetail.setDeleted(agent.getState() == State.DELETED);

        UserDetail userDetail = new UserDetail();
        userDetail.setId(agent.getOwner().getId());
        agentDetail.setOwner(userDetail);

        return agentDetail;
    }

    @Nonnull
    public AgentDetail fillInState(Long userId, AgentDetail agentDetail) {
        agentDetail.setMine(agentDetail.getOwner().getId().equals(userId));
        agentDetail.setDeletable(isDeletable(userId, agentDetail));
        agentDetail.setEditable(isUpdatable(userId, agentDetail));
        return agentDetail;
    }

    public boolean isDeletable(long userId, @Nonnull AgentDetail agentDetail) {
        return agentDetail.getOwner().getId().equals(userId) && !agentDetail.isDeleted();
    }

    public boolean isUpdatable(long userId, @Nonnull AgentDetail agentDetail) {
        return !agentDetail.isDeleted() && agentDetail.getOwner().getId().equals(userId);
    }

    @AgentBasicCacheRefresh
    public AgentDetail refreshBasicCache(long agentId) {
        return findDetail(agentId);
    }

    public Agent findExist(Long agentId) {
        return agentRepository.findByIdAndStateNotIn(agentId, newHashSet(DELETED)).orElseThrow(() -> new NotFoundException(AGENT));
    }
}
