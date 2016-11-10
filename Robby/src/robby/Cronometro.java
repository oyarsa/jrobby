package robby;

import java.time.Duration;
import java.time.Instant;

public class Cronometro {

    private Instant comeco;

    public Cronometro() {
        this.reset();
    }

    public final void reset() {
        this.comeco = Instant.now();
    }

    public long tempo() {
        return Duration.between(comeco, Instant.now()).toMillis();
    }

    public Instant getComeco() {
        return this.comeco;
    }

}
