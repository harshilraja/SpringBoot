package com.tistory.heowc.web;

import com.tistory.heowc.domain.Message;
import com.tistory.heowc.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class MessageHandler {

	@Autowired
	private MessageService service;

	public Mono<ServerResponse> findByOne(ServerRequest request) {
		final Long id = Long.valueOf(request.pathVariable("id"));
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();
		return Mono.fromCompletionStage(service.findByOne(id))
				.flatMap(message -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(message)))
				.switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> add(ServerRequest request) {
		Mono<Message> messageMono = request.bodyToMono(Message.class);
		return Mono.fromCompletionStage(service.add(messageMono.block()))
				.flatMap(message -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(message)));
	}

	public Mono<ServerResponse> modify(ServerRequest request) {
		Mono<Message> messageMono = request.bodyToMono(Message.class);
		return Mono.fromCompletionStage(service.modify(messageMono.block()))
				.flatMap(message -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(message)));
	}

	public Mono<ServerResponse> remove(ServerRequest request) {
		final Long id = Long.valueOf(request.pathVariable("id"));
		service.remove(id);
		return ServerResponse.ok().build();
	}
}