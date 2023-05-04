# kafka compact topic  예제.

topic 생성 설정에서  log.cleanup.policy=compact 와 같이 제공하면 compact topic 이 된다. 

## booting 될 때마다 topic 처음부터(offset : 0) 조회하려면
@KafkaListener 를 구현한 클래스에서 ConsumerSeekAware 중, onPartitionsAssigned() 함수를 override 해야 한다.





