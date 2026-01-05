package com.traceledger.module.production.record;

import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.user.entity.User;

public record BatchCreatedEvent(Batch batch, User user) {}

