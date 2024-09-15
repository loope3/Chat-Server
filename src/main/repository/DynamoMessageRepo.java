import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class DynamoMessageRepo {
    private final DynamoDbTable<Message> messageTable;

    public DynamoMessageRepo(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.messageTable = dynamoDbEnhancedClient.table("MessageHistory", TableSchema.fromBean(MessageEntity.class));
    }

    public void saveMessage(MessageEntity message) {
        messageTable.putItem(message);
    }

    public Optional<MessageEntity> getMessage(String id) {
        MessageEntity message = messageTable.getItem(Key.builder().partitionValue(id).build());
        return Optional.ofNullable(message);
    }

    public List<MessageEntity> getAllMessages() {
        return messageTable.scan()
                .items()
                .stream()
                .collect(Collectors.toList());
    }
}
