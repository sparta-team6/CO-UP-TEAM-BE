package hanghae.api.coupteambe.websocket;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;


@Getter
@Entity
@NoArgsConstructor
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String senderLoginId;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String pjId;


    public ChatMessage(MessageDto messageDto) {
        this.senderLoginId = messageDto.getSenderLoginId();
        this.pjId = messageDto.getPjId();
        this.message = messageDto.getMessage();
    }
}

