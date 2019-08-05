package tma;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class Utils {
  @NotNull
  public static Pageable makePageable(@NotNull Context context) {
    String pageStr = Optional.ofNullable(context.queryParam("page")).orElse("1");
    String sizeStr = Optional.ofNullable(context.queryParam("size")).orElse(Integer.toString(Integer.MAX_VALUE));
    return PageRequest.of(
        Integer.parseInt(pageStr) - 1,
        Integer.parseInt(sizeStr)
    );
  }
}
