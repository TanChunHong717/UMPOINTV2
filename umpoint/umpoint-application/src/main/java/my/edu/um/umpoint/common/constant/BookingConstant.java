package my.edu.um.umpoint.common.constant;

public interface BookingConstant {
    enum BookingStatus {
        PENDING(0),
        REJECT(1),
        APPROVED(2),
        COMPLETED(3),
        CANCELLED(4),;

        private int value;

        BookingStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum PaymentStatus {
        PENDING(0),
        SUCCESS(1),
        FAILED(2),
        REFUNDED(3);

        private int value;

        PaymentStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
