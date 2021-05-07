package callback;

public interface LoginCallback {
  void onSuccess(String username, String password);
  void onFailure();
}
